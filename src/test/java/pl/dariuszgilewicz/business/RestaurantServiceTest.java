package pl.dariuszgilewicz.business;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.AddressRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantOpeningTimeRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantOpeningTimeJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantOpeningTimeEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;
import pl.dariuszgilewicz.infrastructure.security.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pl.dariuszgilewicz.util.AddressFixtures.someAddressEntity3;
import static pl.dariuszgilewicz.util.AddressFixtures.someListOfAddressEntity2;
import static pl.dariuszgilewicz.util.OrdersFixtures.someCustomerOrderNumbers1;
import static pl.dariuszgilewicz.util.OrdersFixtures.someOrdersModelList1;
import static pl.dariuszgilewicz.util.RestaurantFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantOpeningTimeFixtures.someRestaurantOpeningTimeEntity1;
import static pl.dariuszgilewicz.util.RestaurantOpeningTimeFixtures.someRestaurantOpeningTimeEntity2;
import static pl.dariuszgilewicz.util.RestaurantRequestFormFixtures.someRestaurantRequestForm1;
import static pl.dariuszgilewicz.util.UsersFixtures.someBusinessUserModel2;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private RestaurantJpaRepository restaurantJpaRepository;
    @Mock
    private AddressJpaRepository addressJpaRepository;
    @Mock
    private RestaurantEntityMapper restaurantEntityMapper;
    @Mock
    private RestaurantOpeningTimeEntityMapper restaurantOpeningTimeEntityMapper;
    @Mock
    private RestaurantOpeningTimeJpaRepository restaurantOpeningTimeJpaRepository;
    @Mock
    private RestaurantOpeningTimeRepository restaurantOpeningTimeRepository;
    @Mock
    private OrderRepository orderRepository;


    @ParameterizedTest
    @MethodSource("provideRestaurantEmailData")
    void findRestaurantByEmail_worksSuccessfullyForFoundAndNotFoundRestaurant(String restaurantEmail, boolean isFound) {
        //  given
        RestaurantEntity restaurantEntity = someRestaurantEntity3();
        Restaurant expectedRestaurant = someRestaurantModel3();

        //  when
        //  then
        if (isFound) {
            when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(Optional.of(restaurantEntity));
            when(restaurantEntityMapper.mapFromEntity(restaurantEntity)).thenReturn(expectedRestaurant);

            Restaurant resultRestaurant = restaurantService.findRestaurantByEmail(restaurantEmail);

            verify(restaurantEntityMapper, times(1)).mapFromEntity(restaurantEntity);

            assertEquals(expectedRestaurant, resultRestaurant);
        } else {
            when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> restaurantService.findRestaurantByEmail(restaurantEmail))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Restaurant with email: [%s] not found".formatted(restaurantEmail));

            verify(restaurantEntityMapper, never()).mapFromEntity(restaurantEntity);
        }

        verify(restaurantJpaRepository, times(1)).findByEmail(restaurantEmail);
    }

    @ParameterizedTest
    @MethodSource("provideSearchingTermData")
    void findRestaurantsBySearchTerm_worksSuccessfullyForFoundAndNotFoundSearchTerm(String searchTerm, boolean isFound) {
        //  given
        List<Restaurant> expectedRestaurants = someListOfMappedRestaurants2();
        List<AddressEntity> expectedAddressEntities = someListOfAddressEntity2();

        //  when
        //  then
        if(isFound){
            when(addressJpaRepository.findBySearchTerm(searchTerm)).thenReturn(Optional.of(expectedAddressEntities));
            when(restaurantRepository.findRestaurantsByAddress(expectedAddressEntities)).thenReturn(expectedRestaurants);

            List<Restaurant> resultRestaurants = restaurantService.findRestaurantsBySearchTerm(searchTerm);

            verify(restaurantRepository, times(1)).findRestaurantsByAddress(expectedAddressEntities);

            assertEquals(expectedRestaurants, resultRestaurants);
        } else {
            when(addressJpaRepository.findBySearchTerm(searchTerm)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> restaurantService.findRestaurantsBySearchTerm(searchTerm))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Not found List of AddressEntity by searchTerm: [%s]".formatted(searchTerm));

            verify(restaurantRepository, never()).findRestaurantsByAddress(expectedAddressEntities);
        }

        verify(addressJpaRepository, times(1)).findBySearchTerm(searchTerm);
    }

    @Test
    void createRestaurantAndAssignToOwner_shouldWorkSuccessfully() {
        //  given
        User user = someBusinessUserModel2();
        RestaurantRequestForm restaurantRequestForm = someRestaurantRequestForm1();

        //  when
        restaurantService.createRestaurantAndAssignToOwner(restaurantRequestForm, user.getOwner());

        //  then
        ArgumentCaptor<RestaurantRequestForm> restaurantRequestFormArgumentCaptor = ArgumentCaptor.forClass(RestaurantRequestForm.class);
        ArgumentCaptor<RestaurantOwner> restaurantOwnerArgumentCaptor = ArgumentCaptor.forClass(RestaurantOwner.class);

        verify(restaurantRepository, times(1)).createRestaurantFromRestaurantRequest(restaurantRequestFormArgumentCaptor.capture(), restaurantOwnerArgumentCaptor.capture());

        assertEquals(restaurantRequestForm, restaurantRequestFormArgumentCaptor.getValue());
        assertEquals(user.getOwner(), restaurantOwnerArgumentCaptor.getValue());

    }

    @Test
    void updateRestaurantDetails_shouldWorkSuccessfully() {
        //  given
        AddressEntity expectedAddressEntity = someAddressEntity3();
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity3();
        Restaurant expectedRestaurant = someRestaurantModel3();

        when(addressRepository.updateRestaurantAddressDetails(expectedRestaurantEntity.getRestaurantAddress(), expectedRestaurant)).thenReturn(expectedAddressEntity);

        //  when
        restaurantService.updateRestaurantDetails(expectedRestaurantEntity, expectedRestaurant);

        //  then
        ArgumentCaptor<RestaurantEntity> restaurantEntityArgumentCaptor = ArgumentCaptor.forClass(RestaurantEntity.class);
        ArgumentCaptor<Restaurant> restaurantArgumentCaptor = ArgumentCaptor.forClass(Restaurant.class);
        ArgumentCaptor<AddressEntity> addressEntityArgumentCaptor = ArgumentCaptor.forClass(AddressEntity.class);

        verify(restaurantRepository, times(1)).updateRestaurantDetails(
                restaurantEntityArgumentCaptor.capture(),
                restaurantArgumentCaptor.capture(),
                addressEntityArgumentCaptor.capture()
        );

        assertEquals(expectedRestaurantEntity, restaurantEntityArgumentCaptor.getValue());
        assertEquals(expectedRestaurant, restaurantArgumentCaptor.getValue());
        assertEquals(expectedAddressEntity, addressEntityArgumentCaptor.getValue());

    }

    @Test
    void updateRestaurantImage_shouldWorkSuccessfully() {
        //  given
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity4();
        String expectedParam = "CARD";  //  HEADER
        byte[] content = "test image content".getBytes();
        MultipartFile expectedRestaurantImage = new MockMultipartFile("restaurantImage", "test.jpg", "image/jpeg", content);

        //  when
        restaurantService.updateRestaurantImage(expectedRestaurantImage, expectedRestaurantEntity, expectedParam);

        //  then
        ArgumentCaptor<MultipartFile> multipartFileArgumentCaptor = ArgumentCaptor.forClass(MultipartFile.class);
        ArgumentCaptor<RestaurantEntity> restaurantEntityArgumentCaptor = ArgumentCaptor.forClass(RestaurantEntity.class);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(restaurantRepository, times(1)).updateRestaurantImage(
                multipartFileArgumentCaptor.capture(),
                restaurantEntityArgumentCaptor.capture(),
                stringArgumentCaptor.capture()
        );

        assertEquals(expectedRestaurantEntity, restaurantEntityArgumentCaptor.getValue());
        assertEquals(expectedRestaurantImage, multipartFileArgumentCaptor.getValue());
        assertEquals(expectedParam, stringArgumentCaptor.getValue());

    }

    @ParameterizedTest
    @MethodSource("provideOpeningTimeEntityData")
    void updateRestaurantSchedule_shouldWorkSuccessfully(RestaurantOpeningTimeEntity expectedOpeningTimeEntity, boolean isFound) {
        //  given
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity3();
        Restaurant restaurant = someRestaurantModel3();

        when(restaurantOpeningTimeEntityMapper.mapToEntity(restaurant.getRestaurantOpeningTime())).thenReturn(expectedOpeningTimeEntity);

        if(isFound){
            when(restaurantOpeningTimeJpaRepository.findEntityByTimeAndDay(
                    expectedOpeningTimeEntity.getOpeningHour(),
                    expectedOpeningTimeEntity.getCloseHour(),
                    expectedOpeningTimeEntity.getDayOfWeekFrom(),
                    expectedOpeningTimeEntity.getDayOfWeekTill()
            )).thenReturn(Optional.of(expectedOpeningTimeEntity));
        } else {
            when(restaurantOpeningTimeJpaRepository.findEntityByTimeAndDay(
                    expectedOpeningTimeEntity.getOpeningHour(),
                    expectedOpeningTimeEntity.getCloseHour(),
                    expectedOpeningTimeEntity.getDayOfWeekFrom(),
                    expectedOpeningTimeEntity.getDayOfWeekTill()
            )).thenReturn(Optional.empty());
            when(restaurantOpeningTimeRepository.saveAndReturn(expectedOpeningTimeEntity)).thenReturn(expectedOpeningTimeEntity);
        }

        //  when
        restaurantService.updateRestaurantSchedule(expectedRestaurantEntity, restaurant);

        //  then
        ArgumentCaptor<RestaurantEntity> restaurantEntityArgumentCaptor = ArgumentCaptor.forClass(RestaurantEntity.class);
        ArgumentCaptor<RestaurantOpeningTimeEntity> openingTimeEntityArgumentCaptor = ArgumentCaptor.forClass(RestaurantOpeningTimeEntity.class);

        verify(restaurantRepository, times(1)).updateRestaurantSchedule(
                restaurantEntityArgumentCaptor.capture(),
                openingTimeEntityArgumentCaptor.capture()
        );

        assertEquals(expectedRestaurantEntity, restaurantEntityArgumentCaptor.getValue());
        assertEquals(expectedOpeningTimeEntity, openingTimeEntityArgumentCaptor.getValue());
    }

    @Test
    void createOrdersListByOrderNumber_shouldWorkSuccessfully() {
        //  given
        List<Integer> orderNumbers = someCustomerOrderNumbers1();
        List<Orders> expectedOrderList = someOrdersModelList1();

        for (int i = 0; i < orderNumbers.size(); i++) {
            when(orderRepository.findOrderByOrderNumber(orderNumbers.get(i))).thenReturn(expectedOrderList.get(i));
        }

        //  when
        List<Orders> resultOrderList = restaurantService.createOrdersListByOrderNumber(orderNumbers);

        //  then
        assertEquals(expectedOrderList, resultOrderList);
        verify(orderRepository, times(orderNumbers.size())).findOrderByOrderNumber(anyInt());
    }

    private static Stream<Arguments> provideRestaurantEmailData() {
        return Stream.of(
                Arguments.of("karczma@restauracja.pl", true),
                Arguments.of("karma@restauracja.pl", false)
        );
    }

    private static Stream<Arguments> provideSearchingTermData() {
        return Stream.of(
                Arguments.of("Warszawa", true),
                Arguments.of("Poznań", false)
        );
    }

    private static Stream<Arguments> provideOpeningTimeEntityData() {
        return Stream.of(
                Arguments.of(someRestaurantOpeningTimeEntity2(), true),
                Arguments.of(someRestaurantOpeningTimeEntity1(), false)
        );
    }
}