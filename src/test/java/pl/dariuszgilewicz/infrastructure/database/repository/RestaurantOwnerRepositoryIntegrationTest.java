package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import pl.dariuszgilewicz.configuration.AbstractJpaIT;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOwnerJpaRepository;
import pl.dariuszgilewicz.infrastructure.security.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.someRestaurantOwnerEntity1;
import static pl.dariuszgilewicz.util.UsersFixtures.someBusinessUserModel1;

@Import(RestaurantOwnerRepository.class)
class RestaurantOwnerRepositoryIntegrationTest extends AbstractJpaIT {

    @Autowired
    private RestaurantOwnerJpaRepository jpaRepository;
    @Autowired
    private RestaurantOwnerRepository restaurantOwnerRepository;

    @Test
    void findRestaurantOwnerEntityByPesel_shouldWorkSuccessfully() {
        //  given
        RestaurantOwnerEntity expectedRestaurantOwnerEntity = someRestaurantOwnerEntity1();
        String pesel = "91014452879";

        jpaRepository.save(expectedRestaurantOwnerEntity);

        //  when
        RestaurantOwnerEntity resultEntity = restaurantOwnerRepository.findRestaurantOwnerEntityByPesel(pesel);

        //  then
        assertNotNull(resultEntity);
        assertEquals(expectedRestaurantOwnerEntity, resultEntity);

        //  verify persistence
        RestaurantOwnerEntity persistenceEntity = jpaRepository.findByPesel(pesel).orElseThrow(() -> new EntityNotFoundException(
                "Restaurant Owner with pesel: [%s] not found".formatted(pesel)
        ));
        List<RestaurantOwnerEntity> all = jpaRepository.findAll();

        assertNotNull(persistenceEntity);
        assertEquals(resultEntity, persistenceEntity);
        assertEquals(1, all.size());
    }

    @Test
    void findRestaurantOwnerEntityByPesel_shouldThrowExceptionWhenPeselIsNotFound() {
        //  given
        String pesel = "12345678910";
        //  when
        //  then
        assertThrows(EntityNotFoundException.class, () -> restaurantOwnerRepository.findRestaurantOwnerEntityByPesel(pesel));

        //  verify persistence
        List<RestaurantOwnerEntity> all = jpaRepository.findAll();

        assertEquals(0, all.size());
    }

    @Test
    void updateRestaurantOwner_shouldWorkSuccessfully() {
        //  given
        RestaurantOwnerEntity ownerEntity = someRestaurantOwnerEntity1();
        User userForm = someBusinessUserModel1();
        userForm.getOwner().setName("Dawid");
        userForm.getOwner().setSurname("Pracowity");

        jpaRepository.save(ownerEntity);

        //  when
        restaurantOwnerRepository.updateRestaurantOwner(ownerEntity, userForm);

        //  then
        RestaurantOwnerEntity persistenceEntity = jpaRepository.findById(Long.parseLong(ownerEntity.getRestaurantOwnerId().toString())).orElseThrow();
        List<RestaurantOwnerEntity> all = jpaRepository.findAll();

        //  verify persistence
        assertNotNull(persistenceEntity);
        assertEquals(ownerEntity, persistenceEntity);
        assertEquals(1, all.size());

    }

    @Test
    void updateRestaurantOwner_shouldThrowExceptionWhenEntityIsNullValue() {
        //  given
        User userForm = someBusinessUserModel1();

        //  when
        //  then
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> restaurantOwnerRepository.updateRestaurantOwner(null, userForm));

        //  verify persistence
        List<RestaurantOwnerEntity> all = jpaRepository.findAll();
        assertEquals(0, all.size());
    }
}