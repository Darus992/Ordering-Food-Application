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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    public void createUser(User user) {
        userRepository.saveUser(user);
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
        return userRepository.findMappedUserByUserName(userName);
    }
}
