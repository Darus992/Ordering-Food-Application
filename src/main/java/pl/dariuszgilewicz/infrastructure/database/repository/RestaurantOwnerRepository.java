package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOwnerJpaRepository;
import pl.dariuszgilewicz.infrastructure.security.User;

@Repository
@AllArgsConstructor
public class RestaurantOwnerRepository {

    private RestaurantOwnerJpaRepository restaurantOwnerJpaRepository;

    public RestaurantOwnerEntity findRestaurantOwnerEntityByPesel(String pesel) {
        return restaurantOwnerJpaRepository.findByPesel(pesel)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant Owner with pesel: [%s] not found".formatted(pesel)));
    }

    public void updateRestaurantOwner(RestaurantOwnerEntity ownerEntity, User userForm) {
        ownerEntity.setName(userForm.getOwner().getName());
        ownerEntity.setSurname(userForm.getOwner().getSurname());
        ownerEntity.setPesel(userForm.getOwner().getPesel());
        restaurantOwnerJpaRepository.save(ownerEntity);
    }
}
