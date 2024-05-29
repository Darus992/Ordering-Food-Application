package pl.dariuszgilewicz.infrastructure.database.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import pl.dariuszgilewicz.configuration.AbstractJpaIT;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pl.dariuszgilewicz.util.AddressFixtures.someAddressEntity1;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someRestaurantModel1;

@Import(AddressRepository.class)
class AddressRepositoryIntegrationTest extends AbstractJpaIT {

    @Autowired
    private AddressJpaRepository addressJpaRepository;
    @Autowired
    private AddressRepository addressRepository;


    @Test
    void updateRestaurantAddressDetails_shouldWorkSuccessfully() {
        //  given
        Restaurant restaurant = someRestaurantModel1();
        AddressEntity existingAddressEntity = someAddressEntity1();

        addressJpaRepository.save(existingAddressEntity);

        //  when
        AddressEntity updateAddressDetails = addressRepository.updateRestaurantAddressDetails(existingAddressEntity, restaurant);

        //  then
        assertNotNull(updateAddressDetails);
        assertEquals(restaurant.getRestaurantAddress().getCity(), updateAddressDetails.getCity());
        assertEquals(restaurant.getRestaurantAddress().getDistrict(), updateAddressDetails.getDistrict());
        assertEquals(restaurant.getRestaurantAddress().getPostalCode(), updateAddressDetails.getPostalCode());
        assertEquals(restaurant.getRestaurantAddress().getAddressStreet(), updateAddressDetails.getAddress());

        //  Verify persistence
        AddressEntity persistedEntity = addressJpaRepository.findById(updateAddressDetails.getAddressId()).orElseThrow();
        List<AddressEntity> all = addressJpaRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(updateAddressDetails, persistedEntity);
    }

    @Test
    void updateRestaurantAddressDetails_shouldThrowExceptionWhenEntityIsNullValue() {
        //  given
        Restaurant restaurant = someRestaurantModel1();

        //  when
        //  then
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> addressRepository.updateRestaurantAddressDetails(null, restaurant));

        //  verify persistence
        List<AddressEntity> all = addressJpaRepository.findAll();
        assertEquals(0, all.size());

    }
}