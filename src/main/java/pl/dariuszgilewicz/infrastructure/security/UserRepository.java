package pl.dariuszgilewicz.infrastructure.security;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOwnerJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.UserEntityMapper;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final RestaurantOwnerJpaRepository restaurantOwnerJpaRepository;

    public UserEntity saveUser(User user) {
        UserEntity toSave = userEntityMapper.mapToEntity(user);
        toSave.setPassword(passwordEncoder.encode(user.getPassword()));
        userJpaRepository.save(toSave);
        return toSave;
    }

//    public Integer getUserIdByUserEmail(String email) {
//        if (userJpaRepository.findByEmail(email).isEmpty()) {
//            throw new EntityNotFoundException("User with email: [%s] not fount".formatted(email));
//        }
//        return userJpaRepository.findByEmail(email).get().getUserId();
//    }
//
//    public Integer getUserIdByUsername(String userName) {
//        if (userJpaRepository.findByUserName(userName).isEmpty()) {
//            throw new EntityNotFoundException("User with userName: [%s] not fount".formatted(userName));
//        }
//        return userJpaRepository.findByUserName(userName).get().getUserId();
//    }

    public Optional<UserEntity> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    public Optional<UserEntity> findByUserName(String username) {
        return userJpaRepository.findByUserName(username);
    }

    public void assignRestaurantOwner(String userName, String pesel) {
        UserEntity userEntity = userJpaRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND!"));

        RestaurantOwnerEntity ownerEntity = restaurantOwnerJpaRepository.findByPesel(pesel)
                .orElseThrow(() -> new EntityNotFoundException("OWNER NOT FOUND!"));

        userEntity.setOwner(ownerEntity);
        userJpaRepository.saveAndFlush(userEntity);
    }

    public User findMappedUserByUserName(String userName) {
        UserEntity userEntity = userJpaRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND!"));
        return userEntityMapper.mapFromEntity(userEntity);
    }
}
