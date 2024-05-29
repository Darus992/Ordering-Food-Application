package pl.dariuszgilewicz.infrastructure.database.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dariuszgilewicz.configuration.AbstractSpringBootIT;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;
import pl.dariuszgilewicz.infrastructure.util.ImageConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pl.dariuszgilewicz.util.AddressFixtures.someAddressEntity2;
import static pl.dariuszgilewicz.util.AddressFixtures.someAddressEntity4;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.someBusinessRequestForm3;
import static pl.dariuszgilewicz.util.FoodFixtures.someFoodEntity6;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenuEntity4;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenuEntity5;
import static pl.dariuszgilewicz.util.RestaurantFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantOpeningTimeFixtures.someRestaurantOpeningTimeEntity1;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.someRestaurantOwnerEntity2;
import static pl.dariuszgilewicz.util.RestaurantOwnerFixtures.someRestaurantOwnerModel1;
import static pl.dariuszgilewicz.util.RestaurantRequestFormFixtures.someRestaurantRequestForm2;

public class RestaurantRepositoryIntegrationTest extends AbstractSpringBootIT {

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    private RestaurantEntity restaurantEntity;

    @BeforeEach
    void setUp() {
        restaurantEntity = prepareRestaurantEntity();
        restaurantJpaRepository.save(restaurantEntity);
    }

    @AfterEach
    void tearDown() {
        deleteDependencies();
    }


    @Test
    @Transactional
    void findRestaurantEntityByEmail_shouldWorkSuccessfully() {
        //  given
        String restaurantEmail = "karczma@restauracja.pl";

        //  when
        RestaurantEntity resultEntity = restaurantRepository.findRestaurantEntityByEmail(restaurantEmail);

        //  then
        assertNotNull(resultEntity);
        assertEquals(restaurantEntity, resultEntity);
    }

    @Test
    void findRestaurantEntityByEmail_shouldThrowException_whenRestaurantEmailNotExist() {
        //  given
        String nonExistingRestaurantEmail = "kar@restauracja.pl";

        //  when
        //  then
        Assertions.assertThatThrownBy(() -> restaurantRepository.findRestaurantEntityByEmail(nonExistingRestaurantEmail))
                .hasMessageContaining("RestaurantEntity with restaurantEmail: [%s] not found".formatted(nonExistingRestaurantEmail));
    }

    @Test
    @Transactional
    void assignFoodMenuToRestaurant_shouldWorkSuccessfully() {
        //  given
        String restaurantEmail = "karczma@restauracja.pl";
        FoodMenuEntity menuEntity = someFoodMenuEntity5();
        restaurantEntity.setFoodMenu(null);

        //  when
        restaurantRepository.assignFoodMenuToRestaurant(restaurantEmail, menuEntity);

        //  then
        RestaurantEntity persistentEntity = restaurantJpaRepository.findByEmail(restaurantEmail).orElseThrow();

        assertNotNull(persistentEntity);
        assertEquals(restaurantEntity, persistentEntity);
    }

    @Test
    void assignFoodMenuToRestaurant_shouldThrowException_whenRestaurantEmailNotExist() {
        //  given
        String nonExistingRestaurantEmail = "karczma@restacja.pl";
        FoodMenuEntity menuEntity = someFoodMenuEntity5();

        //  when
        //  then
        Assertions.assertThatThrownBy(() -> restaurantRepository.assignFoodMenuToRestaurant(nonExistingRestaurantEmail, menuEntity))
                .hasMessageContaining("Restaurant with email: [%s] not found".formatted(nonExistingRestaurantEmail));
    }

    @Test
    @Transactional
    void assignFoodToFoodMenuInRestaurant_shouldWorkSuccessfully() {
        //  given
        String restaurantEmail = "karczma@restauracja.pl";
        FoodEntity foodEntity = someFoodEntity6();

        //  when
        restaurantRepository.assignFoodToFoodMenuInRestaurant(restaurantEmail, foodEntity);

        //  then
        RestaurantEntity persistedEntity = restaurantJpaRepository.findByEmail(restaurantEmail).orElseThrow();

        assertNotNull(persistedEntity);
        assertEquals(restaurantEntity, persistedEntity);
    }

    @Test
    void assignFoodToFoodMenuInRestaurant_shouldThrowException_whenRestaurantEmailNotExist() {
        //  given
        String nonExistingRestaurantEmail = "kczma@restacja.pl";
        FoodEntity foodEntity = someFoodEntity6();

        //  when
        //  then
        Assertions.assertThatThrownBy(() -> restaurantRepository.assignFoodToFoodMenuInRestaurant(nonExistingRestaurantEmail, foodEntity))
                .hasMessageContaining("Restaurant with email: [%s] not found".formatted(nonExistingRestaurantEmail));
    }

    @Test
    @Transactional
    void findRestaurantsByAddress_shouldWorkSuccessfully() {
        //  given
        List<AddressEntity> addressEntities = List.of(restaurantEntity.getRestaurantAddress());
        RestaurantEntity someRestaurantEntity = someRestaurantEntity5();

        someRestaurantEntity.setFoodMenu(someFoodMenuEntity5());
        someRestaurantEntity.setRestaurantOwner(someRestaurantOwnerEntity2());
        someRestaurantEntity.setCustomerOrders(new ArrayList<>(List.of()));
        restaurantEntity.setCustomerOrders(new ArrayList<>(List.of()));
        restaurantJpaRepository.save(someRestaurantEntity);

        //  when
        //  then
        List<Restaurant> resultsRestaurants = restaurantRepository.findRestaurantsByAddress(addressEntities);
        List<RestaurantEntity> all = restaurantJpaRepository.findAll();

        assertNotNull(resultsRestaurants);
        assertEquals(1, resultsRestaurants.size());
        assertEquals(2, all.size());

        for (Restaurant restaurant : resultsRestaurants) {
            assertEquals(restaurantEntity.getRestaurantName(), restaurant.getRestaurantName());
            assertEquals(restaurantEntity.getPhone(), restaurant.getRestaurantPhone());
            assertEquals(restaurantEntity.getEmail(), restaurant.getRestaurantEmail());
            assertEquals(restaurantEntity.getRestaurantAddress().getCity(), restaurant.getRestaurantAddress().getCity());
            assertEquals(restaurantEntity.getRestaurantAddress().getDistrict(), restaurant.getRestaurantAddress().getDistrict());
            assertEquals(restaurantEntity.getRestaurantAddress().getPostalCode(), restaurant.getRestaurantAddress().getPostalCode());
            assertEquals(restaurantEntity.getRestaurantAddress().getAddress(), restaurant.getRestaurantAddress().getAddressStreet());
        }
    }

    @Test
    void findRestaurantsByAddress_shouldThrowException_whenAddressNotExist() {
        //  given
        AddressEntity nonExistingAddressEntity = someAddressEntity2();
        List<AddressEntity> addressEntities = new ArrayList<>(List.of(nonExistingAddressEntity));

        //  when
        //  then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> restaurantRepository.findRestaurantsByAddress(addressEntities));
    }

    @Test
    @Transactional
    void createRestaurantFromBusinessRequest_shouldWorkSuccessfully() {
        //  given
        RestaurantOwnerEntity owner = restaurantEntity.getRestaurantOwner();
        RestaurantEntity expectedRestaurant = someRestaurantEntity5();
        expectedRestaurant.setRestaurantOwner(owner);
        BusinessRequestForm businessRequestForm = someBusinessRequestForm3();

        //  when
        restaurantRepository.createRestaurantFromBusinessRequest(businessRequestForm, owner);

        //  then
        RestaurantEntity persistentEntity = restaurantJpaRepository.findByEmail(businessRequestForm.getRestaurantEmail()).orElseThrow();
        List<RestaurantEntity> all = restaurantJpaRepository.findAll();

        assertNotNull(persistentEntity);
        assertEquals(expectedRestaurant.getRestaurantName(), persistentEntity.getRestaurantName());
        assertEquals(expectedRestaurant.getPhone(), persistentEntity.getPhone());
        assertEquals(expectedRestaurant.getEmail(), persistentEntity.getEmail());
        assertEquals(expectedRestaurant.getRestaurantOwner().getPesel(), persistentEntity.getRestaurantOwner().getPesel());
        assertEquals(2, all.size());
    }

    @Test
    void createRestaurantFromBusinessRequest_shouldThrowException_whenRequestFormHaveNullValueFile() {
        RestaurantOwnerEntity owner = restaurantEntity.getRestaurantOwner();
        BusinessRequestForm businessRequestForm = someBusinessRequestForm3();
        businessRequestForm.setRestaurantImageCard(null);

        //  when
        //  then
        assertThrows(NullPointerException.class, () -> restaurantRepository.createRestaurantFromBusinessRequest(businessRequestForm, owner));

        List<RestaurantEntity> all = restaurantJpaRepository.findAll();
        assertEquals(1, all.size());
    }

    @Test
    @Transactional
    void createRestaurantFromRestaurantRequest_shouldWorkSuccessfully() {
        //  given
        RestaurantOwner owner = someRestaurantOwnerModel1();
        RestaurantRequestForm restaurantRequestForm = someRestaurantRequestForm2();

        //  when
        restaurantRepository.createRestaurantFromRestaurantRequest(restaurantRequestForm, owner);

        //  then
        RestaurantEntity persistentEntity = restaurantJpaRepository.findByEmail(restaurantRequestForm.getRestaurantEmail()).orElseThrow();
        List<RestaurantEntity> all = restaurantJpaRepository.findAll();

        assertNotNull(persistentEntity);
        assertEquals(restaurantRequestForm.getRestaurantName(), persistentEntity.getRestaurantName());
        assertEquals(restaurantRequestForm.getRestaurantPhone(), persistentEntity.getPhone());
        assertEquals(restaurantRequestForm.getRestaurantEmail(), persistentEntity.getEmail());
        assertEquals(owner.getPesel(), persistentEntity.getRestaurantOwner().getPesel());
        assertEquals(2, all.size());
    }

    @Test
    void createRestaurantFromRestaurantRequest_shouldThrowException_whenRequestFormHaveNullValueFile() {
        //  given
        RestaurantOwner owner = someRestaurantOwnerModel1();
        RestaurantRequestForm restaurantRequestForm = someRestaurantRequestForm2();
        restaurantRequestForm.setRestaurantImageHeader(null);

        //  when
        //  then
        assertThrows(NullPointerException.class, () -> restaurantRepository.createRestaurantFromRestaurantRequest(restaurantRequestForm, owner));

        List<RestaurantEntity> all = restaurantJpaRepository.findAll();
        assertEquals(1, all.size());
    }

    @Test
    @Transactional
    void updateRestaurantDetails_shouldWorkSuccessfully() {
        //  given
        Restaurant restaurant = someRestaurantModel2();
        AddressEntity addressEntity = someAddressEntity4();

        //  when
        restaurantRepository.updateRestaurantDetails(restaurantEntity, restaurant, addressEntity);

        //  then
        RestaurantEntity persistentEntity = restaurantJpaRepository.findByEmail(restaurant.getRestaurantEmail()).orElseThrow();
        List<RestaurantEntity> all = restaurantJpaRepository.findAll();

        assertNotNull(persistentEntity);
        assertEquals(restaurantEntity, persistentEntity);
        assertEquals(1, all.size());
    }

    @Test
    void updateRestaurantDetails_shouldThrowException_whenUpdatedFileIsNullValue() {
        //  given
        Restaurant restaurant = someRestaurantModel2();
        AddressEntity addressEntity = someAddressEntity4();
        String restaurantEmail = "karczma@restauracja.pl";

        restaurant.setRestaurantPhone(null);
        restaurant.setRestaurantEmail(null);

        //  when
        //  then
        assertThrows(DataIntegrityViolationException.class, () -> restaurantRepository.updateRestaurantDetails(restaurantEntity, restaurant, addressEntity));
        RestaurantEntity persistentEntity = restaurantJpaRepository.findByEmail(restaurantEmail).orElseThrow();

        assertNotNull(persistentEntity.getPhone());
        assertNotNull(persistentEntity.getEmail());
        assertNotEquals(persistentEntity.getRestaurantName(), restaurant.getRestaurantName());
    }

    @ParameterizedTest
    @CsvSource({
            "CARD",
            "HEADER"
    })
    @Transactional
    void updateRestaurantImage_shouldWorkSuccessfully(String param) throws IOException {
        // given
        MultipartFile mockMultipartFile = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg", "dummyImageContent".getBytes());
        byte[] expectedBytes = ImageConverter.convertFileToBytes(mockMultipartFile);

        // when
        restaurantRepository.updateRestaurantImage(mockMultipartFile, restaurantEntity, param);

        // then
        RestaurantEntity updatedEntity = restaurantJpaRepository.findByEmail(restaurantEntity.getEmail()).orElseThrow();


        if ("CARD".equals(param)) {
            assertArrayEquals(expectedBytes, updatedEntity.getRestaurantImageCard());
        } else {
            assertArrayEquals(expectedBytes, updatedEntity.getRestaurantImageHeader());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "CARD",
            "HEADER"
    })
    @Transactional
    void updateRestaurantImage_shouldThrowException_whenImageConversionFails(String param) {
        //  given
        //  when
        //  then
        assertThrows(RuntimeException.class, () -> restaurantRepository.updateRestaurantImage(null, restaurantEntity, param));
    }

    @Test
    @Transactional
    void updateRestaurantSchedule_shouldWorkSuccessfully() {
        //  given
        RestaurantOpeningTimeEntity openingTimeEntity = someRestaurantOpeningTimeEntity1();

        //  when
        restaurantRepository.updateRestaurantSchedule(restaurantEntity, openingTimeEntity);

        //  then
        RestaurantEntity persistentEntity = restaurantJpaRepository.findByEmail(restaurantEntity.getEmail()).orElseThrow();

        assertNotNull(persistentEntity);
        assertEquals(restaurantEntity, persistentEntity);
    }

    @Test
    void updateRestaurantSchedule_shouldThrowException_whenUpdatedTimeEntityHaveNullFileValue() {
        //  given
        RestaurantOpeningTimeEntity openingTimeEntity = someRestaurantOpeningTimeEntity1();
        openingTimeEntity.setDayOfWeekFrom(null);

        //  when
        //  then
        assertThrows(DataIntegrityViolationException.class, () -> restaurantRepository.updateRestaurantSchedule(restaurantEntity, openingTimeEntity));
    }

    private RestaurantEntity prepareRestaurantEntity() {
        RestaurantEntity entity = someRestaurantEntity3();
        entity.setFoodMenu(someFoodMenuEntity4());
        return entity;
    }

    private void deleteDependencies() {
        restaurantJpaRepository.deleteAll();
    }
}
