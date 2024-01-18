package pl.dariuszgilewicz.infrastructure.security;

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
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;


    @Transactional
    public void createCustomerUser(CustomerRequestForm customerRequestForm) {
        userRepository.createCustomerUser(customerRequestForm);
    }

    @Transactional
    public void createBusinessUser(BusinessRequestForm businessRequestForm) {
        userRepository.createBusinessUser(businessRequestForm);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserName(username)
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
        UserEntity userEntity = userRepository.findUserEntityByUsername(userName);
        return userEntityMapper.mapFromEntity(userEntity);

//        //  TODO: ZASTANOWIĆ SIE NAD TYM CZY TO ZOSTAWIĆ CZY USUNĄĆ
//
//        if(userEntity.getCustomer() == null && userEntity.getOwner() == null){
//            user.setIsCompleted(false);
//        }else {
//            user.setIsCompleted(true);
//        }
//        return user;
    }

    public static boolean checkIfIsAuthenticated(Model model, Authentication authentication) {
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuthenticated);
        return isAuthenticated;
    }

    public static void assignRoleDependsOnAuthentication(Authentication authentication, Model model, boolean isAuthenticated) {
        if (isAuthenticated) {
            String userRole = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(Objects::toString)
                    .orElse(null);
            model.addAttribute("userRole", userRole);
        }
    }
}
