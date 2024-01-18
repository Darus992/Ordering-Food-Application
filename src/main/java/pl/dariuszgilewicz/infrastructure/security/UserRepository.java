package pl.dariuszgilewicz.infrastructure.security;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.CustomerRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantOwnerRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.UserEntityMapper;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final RestaurantRepository restaurantRepository;


    public Optional<UserEntity> findByUserName(String username) {
        return userJpaRepository.findByUserName(username);
    }

    public UserEntity findUserEntityByUsername(String username) {
        return userJpaRepository.findByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username: [%s] not found".formatted(username)));
    }

    public void createCustomerUser(CustomerRequestForm customerRequestForm) {
        UserEntity toSave = userEntityMapper.mapFromCustomerRequest(customerRequestForm);
        toSave.setPassword(passwordEncoder.encode(customerRequestForm.getUserPassword()));
        userJpaRepository.save(toSave);
    }

    public void createBusinessUser(BusinessRequestForm businessRequestForm) {
        UserEntity toSave = userEntityMapper.mapFromBusinessRequest(businessRequestForm);
        toSave.setPassword(passwordEncoder.encode(businessRequestForm.getUserPassword()));
        RestaurantOwnerEntity owner = toSave.getOwner();
        restaurantRepository.createRestaurantFromBusinessRequest(businessRequestForm, owner);
        userJpaRepository.save(toSave);
    }
}
