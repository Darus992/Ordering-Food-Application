package pl.dariuszgilewicz.infrastructure.security;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import pl.dariuszgilewicz.infrastructure.database.repository.CustomerRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantOwnerRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.UserEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.exception.EntityAlreadyExistAuthenticationException;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;

    private final RestaurantOwnerRepository restaurantOwnerRepository;
    private final CustomerRepository customerRepository;


    @Transactional
    public void createCustomerUser(CustomerRequestForm customerRequestForm) {
        if (userJpaRepository.findByEmail(customerRequestForm.getUserEmail()).isPresent()) {
            throw new EntityAlreadyExistAuthenticationException("User with email: [%s] already exist".formatted(customerRequestForm.getUserEmail()));
        }
        userRepository.createCustomerUser(customerRequestForm);
    }

    @Transactional
    public void createBusinessUser(BusinessRequestForm businessRequestForm) {
        if (userJpaRepository.findByEmail(businessRequestForm.getUserEmail()).isPresent()) {
            throw new EntityAlreadyExistAuthenticationException("User with email: [%s] already exist".formatted(businessRequestForm.getUserEmail()));
        }
        userRepository.createBusinessUser(businessRequestForm);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: [%s] not found".formatted(username)));
        List<GrantedAuthority> authorities = getUserAuthority(userEntity.getRoles());
        return buildUserForAuthentication(userEntity, authorities);
    }

    @Transactional
    public void updateUserData(User userForm, String userEmail) {
        Optional<UserEntity> optionalUserEntity = userJpaRepository.findByEmail(userEmail);

        if (optionalUserEntity.isEmpty()) {
            throw new EntityNotFoundException("UserEntity with email: [%s] not found.");
        }

        UserEntity userEntity = optionalUserEntity.get();
        userEntity.setUsername(userForm.getUsername());
        userEntity.setEmail(userForm.getEmail());

        if (userEntity.getOwner() != null) {
            restaurantOwnerRepository.updateRestaurantOwner(userEntity.getOwner(), userForm);
        } else if (userEntity.getCustomer() != null) {
            customerRepository.updateCustomer(userEntity.getCustomer(), userForm);
        }

        userRepository.updateUserData(userEntity, userForm.getPassword());
    }

    @Transactional
    public void deleteAccount(String userEmail) {
        Optional<UserEntity> optional = userJpaRepository.findByEmail(userEmail);

        if (optional.isEmpty()) {
            throw new EntityNotFoundException("UserEntity with email: [%s] not found.".formatted(userEmail));
        }

        UserEntity userEntity = optional.get();
        userJpaRepository.delete(userEntity);
    }

    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getPrincipal();
        return ((org.springframework.security.core.userdetails.User) details).getUsername();
    }

    public Optional<User> getCurrentOptionalUser(Model model) {
        String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("User is not log in.");
        Optional<User> optionalUser;
        boolean isAuthenticated;

        isAuthenticated = authority.equals(UserRole.CUSTOMER.name()) || authority.equals(UserRole.OWNER.name());

        if (isAuthenticated) {
            String username = getCurrentUserName();
            UserEntity userEntity = userRepository.findUserEntityByUsername(username);
            optionalUser = userEntityMapper.mapFromEntityToOptionalUser(userEntity);
            model.addAttribute("isAuthenticated", true);
            return optionalUser;
        } else {
            model.addAttribute("isAuthenticated", false);
            return Optional.empty();
        }
    }

    private List<GrantedAuthority> getUserAuthority(Set<UserRole> userRoles) {
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .distinct()
                .collect(Collectors.toList());
    }

    private UserDetails buildUserForAuthentication(UserEntity userEntity, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getActive(),
                true,
                true,
                true,
                authorities
        );
    }

    public boolean checkIfIsAuthenticated(Model model, Authentication authentication) {
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuthenticated);
        return isAuthenticated;
    }

    public void assignRoleDependsOnAuthentication(Authentication authentication, Model model, boolean isAuthenticated) {
        if (isAuthenticated) {
            String userRole = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(Objects::toString)
                    .orElse(null);
            model.addAttribute("userRole", userRole);
        }
    }
}
