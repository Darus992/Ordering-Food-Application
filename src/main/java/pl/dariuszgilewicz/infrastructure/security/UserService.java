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
        UserEntity userEntity = userJpaRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: [%s] not found".formatted(username)));
        List<GrantedAuthority> authorities = getUserAuthority(userEntity.getRoles());
        return buildUserForAuthentication(userEntity, authorities);
    }

    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getPrincipal();
        return ((org.springframework.security.core.userdetails.User) details).getUsername();
    }

    private List<GrantedAuthority> getUserAuthority(Set<UserRole> userRoles) {
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .distinct()
                .collect(Collectors.toList());
    }

    private UserDetails buildUserForAuthentication(UserEntity userEntity, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(
                userEntity.getUserName(),
                userEntity.getPassword(),
                userEntity.getActive(),
                true,
                true,
                true,
                authorities
        );
    }

    public User findUserByUserName(String userName) {
        return userJpaRepository.findByUserName(userName)
                .map(userEntityMapper::mapFromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User Entity with username: [%s] not found".formatted(userName)
                ));
        //  TODO: ZASTANOWIĆ SIE NAD TYM CZY TO ZOSTAWIĆ CZY USUNĄĆ
//
//        if(userEntity.getCustomer() == null && userEntity.getOwner() == null){
//            user.setIsCompleted(false);
//        }else {
//            user.setIsCompleted(true);
//        }
//        return user;
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
