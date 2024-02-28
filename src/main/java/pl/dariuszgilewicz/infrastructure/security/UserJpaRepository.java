package pl.dariuszgilewicz.infrastructure.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.security.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

//    @Query("""
//            SELECT user FROM UserEntity user
//            WHERE user.email = :email
//            """)
//    UserEntity findByEmail(final @Param("email") String email);

//    @Query("""
//            SELECT user FROM UserEntity user
//            WHERE user.userName = :userName
//            """)
//    Optional<UserEntity> findByUserName(final @Param("userName") String userName);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUserName(String userName);
}
