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
import pl.dariuszgilewicz.infrastructure.model.Orders;
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

    private User findUserOwnerByUserName(String userName) {
        return userJpaRepository.findByUserName(userName)
                .map(userEntityMapper::mapFromEntityOwner)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User Entity with username: [%s] not found".formatted(userName)
                ));
    }

    private User findUserCustomerByUserName(String userName) {
        return userJpaRepository.findByUserName(userName)
                .map(userEntityMapper::mapFromEntityCustomer)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User Entity with username: [%s] not found".formatted(userName)
                ));
    }

    public User getCurrentUser() {
        String username = getCurrentUserName();
        Optional<? extends GrantedAuthority> authority = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .findFirst();

        if (authority.isPresent() && authority.get().getAuthority().equals(UserRole.CUSTOMER.toString())) {
            User user = findUserCustomerByUserName(username);
            boolean isCartEmpty = checkIfCustomerCartIsEmpty(user.getCustomer().getCustomerOrders());
            user.getCustomer().setCartEmpty(isCartEmpty);
            return user;
        } else if (authority.isPresent() && authority.get().getAuthority().equals(UserRole.OWNER.toString())) {
            return findUserOwnerByUserName(username);
        } else {
            throw new RuntimeException("YOU ARE NOT LOGG IN");
        }
    }

    private boolean checkIfCustomerCartIsEmpty(List<Orders> customerOrders) {
        int counter = 0;
        for (Orders order : customerOrders){
            if(order.getStatus() != null){
                counter++;
            }
        }
        return customerOrders.size() == counter;
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
