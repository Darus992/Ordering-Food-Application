package pl.dariuszgilewicz.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;

@Repository
@AllArgsConstructor
public class AddressRepository {

    private AddressJpaRepository addressJpaRepository;

    public AddressEntity updateRestaurantAddressDetails(AddressEntity restaurantAddress, Restaurant restaurant) {
        restaurantAddress.setCity(restaurant.getRestaurantAddress().getCity());
        restaurantAddress.setDistrict(restaurant.getRestaurantAddress().getDistrict());
        restaurantAddress.setPostalCode(restaurant.getRestaurantAddress().getPostalCode());
        restaurantAddress.setAddress(restaurant.getRestaurantAddress().getAddressStreet());
        addressJpaRepository.save(restaurantAddress);
        return restaurantAddress;
    }
}
