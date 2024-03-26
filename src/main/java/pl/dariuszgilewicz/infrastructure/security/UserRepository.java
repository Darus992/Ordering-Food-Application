package pl.dariuszgilewicz.infrastructure.security;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOwnerJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.UserEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.exception.EntityAlreadyExistAuthenticationException;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.util.FileUploadUtil;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    public static final String RESTAURANT_IMAGE_DIR = "src/main/resources/static/images/restaurant/";
    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantOwnerJpaRepository restaurantOwnerJpaRepository;
    private final RestaurantJpaRepository restaurantJpaRepository;


    public UserEntity findUserEntityByUsername(String username){
        return userJpaRepository.findByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "UserEntity with username: [%s] not found".formatted(username)
                ));
    }

    public void createCustomerUser(CustomerRequestForm customerRequestForm) {
        UserEntity toSave = userEntityMapper.mapFromCustomerRequest(customerRequestForm);
        toSave.setPassword(passwordEncoder.encode(customerRequestForm.getUserPassword()));
        userJpaRepository.save(toSave);
    }

    public void createBusinessUser(BusinessRequestForm businessRequestForm) {
        validateExistingEntities(businessRequestForm);

//        MultipartFile image = businessRequestForm.getRestaurantImageCard();
//        String storageFileName = FileUploadUtil.uploadFile(image, RESTAURANT_IMAGE_DIR, null);

        UserEntity toSave = userEntityMapper.mapFromBusinessRequest(businessRequestForm);
        toSave.setPassword(passwordEncoder.encode(businessRequestForm.getUserPassword()));

        RestaurantOwnerEntity owner = toSave.getOwner();
        restaurantRepository.createRestaurantFromBusinessRequest(businessRequestForm, owner);

        userJpaRepository.save(toSave);
    }

    private void validateExistingEntities(BusinessRequestForm businessRequestForm) {
        String restaurantEmail = businessRequestForm.getRestaurantEmail();
        String ownerPesel = businessRequestForm.getOwnerPesel();

        if (restaurantJpaRepository.existsByEmail(restaurantEmail) || restaurantOwnerJpaRepository.existsByPesel(ownerPesel)) {
            throw new EntityAlreadyExistAuthenticationException(
                    "Restaurant Entity with email: [%s] or Restaurant Owner Entity with pesel: [%s] already exist"
                            .formatted(restaurantEmail, ownerPesel)
            );
        }
    }
}
