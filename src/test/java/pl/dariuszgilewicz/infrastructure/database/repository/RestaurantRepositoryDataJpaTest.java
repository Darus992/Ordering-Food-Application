package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.dariuszgilewicz.configuration.AbstractJpaIT;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOwnerJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someRestaurantEntity3;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.someRestaurantOwnerEntity1;
import static pl.dariuszgilewicz.util.RestaurantRequestFormFixtures.someRestaurantRequestForm1;


@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RestaurantRepositoryDataJpaTest extends AbstractJpaIT {

    private RestaurantOwnerJpaRepository restaurantOwnerJpaRepository;
    private RestaurantJpaRepository restaurantJpaRepository;

    @MockBean
    private RestaurantOwnerRepository restaurantOwnerRepository;

    @MockBean
    private RestaurantEntityMapper restaurantEntityMapper;


    @AfterEach
    void after() {
        restaurantOwnerJpaRepository.deleteAll();
    }

    @Test
    void thisShouldSaveRestaurantEntityCorrectly() {
        //  given
        RestaurantRequestForm requestForm = someRestaurantRequestForm1();
        RestaurantEntity restaurantEntity = someRestaurantEntity3();
        String pesel = "91014452879";

        //  when
        restaurantJpaRepository.save(restaurantEntity);

        RestaurantOwnerEntity ownerResult = restaurantOwnerJpaRepository.findByPesel(pesel)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant Owner with pesel: [%s] not found".formatted(pesel)));

        List<RestaurantEntity> restaurants = restaurantJpaRepository.findAllByRestaurantName(requestForm.getRestaurantName())
                .orElseThrow(() -> new EntityNotFoundException("Not found restaurants list with restaurant name: [%s]".formatted(requestForm.getRestaurantName())));

        //  then
        assertNotNull(ownerResult);
        assertNotNull(restaurants);
        assertEquals(restaurants.get(0).getRestaurantOwner(), ownerResult);
        assertEquals(1, restaurants.size());
    }

    @Test
    void createRestaurantFromRestaurantRequest_shouldWorkCorrectly(){
        //  given
        RestaurantRequestForm requestForm = someRestaurantRequestForm1();
        RestaurantEntity restaurantEntity = someRestaurantEntity3();
        RestaurantOwnerEntity restaurantOwnerEntity = someRestaurantOwnerEntity1();
        String ownerPesel = restaurantOwnerEntity.getPesel();
        when(restaurantOwnerRepository.findRestaurantOwnerEntityByPesel(ownerPesel)).thenReturn(restaurantOwnerEntity);
        when(restaurantEntityMapper.mapFromRestaurantRequest(requestForm, restaurantOwnerEntity)).thenReturn(restaurantEntity);

        //  when
        restaurantJpaRepository.save(restaurantEntity);
        RestaurantEntity result = restaurantJpaRepository.findByEmail(restaurantEntity.getEmail()).get();

        //  then
        assertNotNull(result);
        assertEquals(restaurantEntity.getRestaurantName(), result.getRestaurantName());
        assertEquals(restaurantEntity.getPhone(), result.getPhone());
        assertEquals(restaurantEntity.getEmail(), result.getEmail());
    }
}
