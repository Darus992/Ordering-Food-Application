package pl.dariuszgilewicz.api.controller.rest;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.dariuszgilewicz.api.dto.OrderDTO;
import pl.dariuszgilewicz.api.dto.RestaurantDTO;
import pl.dariuszgilewicz.api.dto.mapper.BusinessUserMapper;
import pl.dariuszgilewicz.api.dto.mapper.FoodMapper;
import pl.dariuszgilewicz.api.dto.mapper.OrderMapper;
import pl.dariuszgilewicz.api.dto.mapper.RestaurantMapper;
import pl.dariuszgilewicz.api.dto.request.FoodRequestFormDTO;
import pl.dariuszgilewicz.business.FoodMenuService;
import pl.dariuszgilewicz.business.OrderService;
import pl.dariuszgilewicz.business.RestaurantService;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.enums.AvailableOrderStatuses;
import pl.dariuszgilewicz.infrastructure.database.enums.RestaurantImageType;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOpeningTime;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dariuszgilewicz.util.FoodFixtures.someFoodModel5;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenuEntity1;
import static pl.dariuszgilewicz.util.FoodRequestFormDTOFixtures.someFoodRequestDTO1;
import static pl.dariuszgilewicz.util.OrdersFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantOpeningTimeFixtures.someRestaurantOpeningTimeModel2;
import static pl.dariuszgilewicz.util.RestaurantOpeningTimeFixtures.someRestaurantOpeningTimeModel3;
import static pl.dariuszgilewicz.util.UsersFixtures.someBusinessUserModel1;

@WebMvcTest(controllers = RestaurantOwnerRestController.class)
@Import(SecurityConfiguration.class)
class RestaurantOwnerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private RestaurantMapper restaurantMapper;
    @MockBean
    private OrderMapper orderMapper;
    @MockBean
    private BusinessUserMapper businessUserMapper;
    @MockBean
    private FoodMapper foodMapper;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OrderService orderService;
    @MockBean
    private RestaurantJpaRepository restaurantJpaRepository;
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private FoodMenuService foodMenuService;

    public static final String BASE_PATH = "/api/owner";
    public static final String BASE_URL = "http://localhost:8190/ordering-food-application";


    @Test
    void thatShouldShowAllOwnerRestaurantsCorrectly() throws Exception {
        //  given
        ObjectMapper objectMapper = new ObjectMapper();
        User user = someBusinessUserModel1();
        List<RestaurantDTO> restaurantDTOS = someListOfRestaurantDTO1();

        when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));
        when(restaurantMapper.mapToDTOList(user.getOwner().getRestaurants(), BASE_URL)).thenReturn(restaurantDTOS);

        //  when
        //  then
        String content = mockMvc.perform(get(BASE_PATH))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        List<RestaurantDTO> result = objectMapper.readValue(content, new TypeReference<List<RestaurantDTO>>() {
        });

        for (int i = 0; i < restaurantDTOS.size(); i++) {
            assertEquals(restaurantDTOS.get(i).getRestaurantImageCard(), result.get(i).getRestaurantImageCard());
            assertEquals(restaurantDTOS.get(i).getRestaurantImageHeader(), result.get(i).getRestaurantImageHeader());
            assertEquals(restaurantDTOS.get(i).getRestaurantName(), result.get(i).getRestaurantName());
            assertEquals(restaurantDTOS.get(i).getRestaurantPhone(), result.get(i).getRestaurantPhone());
            assertEquals(restaurantDTOS.get(i).getRestaurantEmail(), result.get(i).getRestaurantEmail());
            assertEquals(restaurantDTOS.get(i).getRestaurantCity(), result.get(i).getRestaurantCity());
            assertEquals(restaurantDTOS.get(i).getRestaurantDistrict(), result.get(i).getRestaurantDistrict());
            assertEquals(restaurantDTOS.get(i).getRestaurantPostalCode(), result.get(i).getRestaurantPostalCode());
            assertEquals(restaurantDTOS.get(i).getRestaurantAddressStreet(), result.get(i).getRestaurantAddressStreet());
            assertEquals(restaurantDTOS.get(i).getOpeningHour(), result.get(i).getOpeningHour());
            assertEquals(restaurantDTOS.get(i).getCloseHour(), result.get(i).getCloseHour());
            assertEquals(restaurantDTOS.get(i).getDayOfWeekFrom(), result.get(i).getDayOfWeekFrom());
            assertEquals(restaurantDTOS.get(i).getDayOfWeekTill(), result.get(i).getDayOfWeekTill());
        }
    }

    @ParameterizedTest
    @MethodSource("provideData")
    void thatShouldShowRestaurantOrderDetailsCorrectly(
            String restaurantEmail,
            String orderNumber,
            Orders order,
            OrderDTO orderDTO,
            RestaurantDTO restaurantDTO,
            boolean isDataFound
    ) throws Exception {
        //  given
        User user = someBusinessUserModel1();

        when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));
        setOrderNumberToRestaurantAndDTO(user, restaurantDTO, restaurantEmail, Integer.parseInt(orderNumber), isDataFound);

        //  when
        //  then
        if (isDataFound) {
            when(orderRepository.findOrderByOrderNumber(Integer.parseInt(orderNumber))).thenReturn(order);
            when(orderMapper.mapToDTO(order)).thenReturn(orderDTO);

            mockMvc.perform(get(BASE_PATH + "/restaurant/{restaurantEmail}/order-details/{orderNumber}",
                            restaurantEmail, orderNumber))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orderNumber", Matchers.is(orderDTO.getOrderNumber())))
                    .andExpect(jsonPath("$.orderTotalPrice", Matchers.is(orderDTO.getOrderTotalPrice())))
                    .andExpect(jsonPath("$.customerAddressCity", Matchers.is(orderDTO.getCustomerAddressCity())));
        } else {
            mockMvc.perform(get(BASE_PATH + "/restaurant/{restaurantEmail}/order-details/{orderNumber}",
                            restaurantEmail, orderNumber))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @ParameterizedTest
    @MethodSource("provideListOfRestaurantEmails")
    void thatUpdateOrderStatusShouldWordCorrectly(String restaurantEmail, String orderNumber, boolean isContain) throws Exception {
        //  given
        User user = someBusinessUserModel1();
        AvailableOrderStatuses availableOrderStatuses = AvailableOrderStatuses.ON_THE_WAY;

        when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));

        Optional<Restaurant> optionalRestaurant = findOptionalRestaurantByEmailFromUserOwner(restaurantEmail, user);
        optionalRestaurant
                .ifPresent(restaurant -> restaurant.setCustomerOrdersNumbers(List.of(Integer.parseInt(orderNumber))));


        //  when
        //  then
        if (isContain) {
            mockMvc.perform(patch(BASE_PATH + "/restaurant/{restaurantEmail}/order-update", restaurantEmail)
                            .param("orderNumber", orderNumber)
                            .param("availableOrderStatuses", availableOrderStatuses.name())
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string("Order status changed successfully"));
        } else {
            mockMvc.perform(patch(BASE_PATH + "/restaurant/{restaurantEmail}/order-update", restaurantEmail)
                            .param("orderNumber", orderNumber)
                            .param("availableOrderStatuses", availableOrderStatuses.name())
                    )
                    .andExpect(status().isNoContent());
        }
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForOwnerProfile")
    void thatUpdateOwnerProfileShouldWorkCorrectly(
            String username,
            String email,
            String ownerName,
            String ownerSurname,
            String password,
            String ownerPesel
    ) throws Exception {
        //  given
        User user = someBusinessUserModel1();

        when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));
        when(businessUserMapper.mapArgumentToUser(username, email, ownerName, ownerSurname, password, ownerPesel, user)).thenCallRealMethod();

        //  when
        //  then
        mockMvc.perform(patch(BASE_PATH + "/update-profile")
                        .param("username", username)
                        .param("email", email)
                        .param("ownerName", ownerName)
                        .param("ownerSurname", ownerSurname)
                        .param("password", password)
                        .param("ownerPesel", ownerPesel)
                )
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Owner profile updated successfully"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForRestaurant")
    void thatUpdateRestaurantDetailsShouldWorkCorrectly(
            String restaurantEmail,
            RestaurantEntity foundedRestaurant,
            String restaurantName,
            String restaurantPhone,
            String restaurantEmailToUpdate,
            String restaurantAddressCity,
            String restaurantAddressDistrict,
            String restaurantAddressPostalCode,
            String restaurantAddressStreet,
            boolean isValid
    ) throws Exception {
        //  given
        User user = someBusinessUserModel1();
        Optional<RestaurantEntity> optionalRestaurant = foundedRestaurant != null ? Optional.of(foundedRestaurant) : Optional.empty();

        when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));
        when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(optionalRestaurant);
        when(restaurantMapper.mapRestaurantDetailsFormToUpdateFromDTO(
                restaurantName,
                restaurantPhone,
                restaurantEmailToUpdate,
                restaurantAddressCity,
                restaurantAddressDistrict,
                restaurantAddressPostalCode,
                restaurantAddressStreet,
                foundedRestaurant
        )).thenCallRealMethod();

        //  when
        //  then
        if (isValid) {
            mockMvc.perform(patch(BASE_PATH + "/restaurant/{restaurantEmail}/details", restaurantEmail)
                            .param("restaurantName", restaurantName)
                            .param("restaurantPhone", restaurantPhone)
                            .param("restaurantEmailToUpdate", restaurantEmailToUpdate)
                            .param("restaurantAddressCity", restaurantAddressCity)
                            .param("restaurantAddressDistrict", restaurantAddressDistrict)
                            .param("restaurantAddressPostalCode", restaurantAddressPostalCode)
                            .param("restaurantAddressStreet", restaurantAddressStreet)
                    )
                    .andExpect(content().string("Restaurant details updated successfully."))
                    .andExpect(status().isOk());

            verify(restaurantService, times(1)).updateRestaurantDetails(any(RestaurantEntity.class), any(Restaurant.class));

        } else {
            mockMvc.perform(patch(BASE_PATH + "/restaurant/{restaurantEmail}/details", restaurantEmail)
                            .param("restaurantName", restaurantName)
                            .param("restaurantPhone", restaurantPhone)
                            .param("restaurantEmailToUpdate", restaurantEmailToUpdate)
                            .param("restaurantAddressCity", restaurantAddressCity)
                            .param("restaurantAddressDistrict", restaurantAddressDistrict)
                            .param("restaurantAddressPostalCode", restaurantAddressPostalCode)
                            .param("restaurantAddressStreet", restaurantAddressStreet)
                    )
                    .andExpect(status().isBadRequest());

            verify(restaurantService, never()).updateRestaurantDetails(any(RestaurantEntity.class), any(Restaurant.class));

        }
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForFoodToCreate")
    void thatShouldCreateFoodAndAssignToFoodMenuCorrectly(
            String restaurantEmail,
            RestaurantEntity foundedRestaurant,
            FoodRequestFormDTO requestFormDTO,
            Food mappedFood,
            boolean isValid
    ) throws Exception {
        //  given
        User user = someBusinessUserModel1();

        when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));

        //  when
        //  then
        if (isValid) {
            when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(Optional.of(foundedRestaurant));
            when(foodMapper.mapFromFoodRequestFormDTO(requestFormDTO)).thenReturn(mappedFood);

            mockMvc.perform(multipart(BASE_PATH + "/restaurant/{restaurantEmail}/create-food", restaurantEmail)
                            .file("fileImageToUpload", requestFormDTO.getFileImageToUpload().getBytes())
                            .param("foodCategory", requestFormDTO.getFoodCategory())
                            .param("foodName", requestFormDTO.getFoodName())
                            .param("foodDescription", requestFormDTO.getFoodDescription())
                            .param("foodPrice", requestFormDTO.getFoodPrice())
                    )
                    .andExpect(content().string("Food created and add to food menu successfully"))
                    .andExpect(status().isOk());

            ArgumentCaptor<Food> foodArgumentCaptor = ArgumentCaptor.forClass(Food.class);
            ArgumentCaptor<RestaurantEntity> restaurantEntityArgumentCaptor = ArgumentCaptor.forClass(RestaurantEntity.class);

            verify(foodMenuService, times(1)).assignFoodToFoodMenu(foodArgumentCaptor.capture(), restaurantEntityArgumentCaptor.capture());
        } else {
            mockMvc.perform(multipart(BASE_PATH + "/restaurant/{restaurantEmail}/create-food", restaurantEmail)
                            .file("fileImageToUpload", requestFormDTO.getFileImageToUpload().getBytes())
                            .param("foodCategory", requestFormDTO.getFoodCategory())
                            .param("foodName", requestFormDTO.getFoodName())
                            .param("foodDescription", requestFormDTO.getFoodDescription())
                            .param("foodPrice", requestFormDTO.getFoodPrice())
                    )
                    .andExpect(status().isNotFound());

            verify(foodMenuService, never()).assignFoodToFoodMenu(mappedFood, foundedRestaurant);
        }
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForRestaurantSchedule")
    void thatUpdateRestaurantScheduleShouldWorkCorrectly(
            String restaurantEmail,
            RestaurantEntity foundedRestaurant,
            Restaurant updatedRestaurant,
            RestaurantOpeningTime updatedOpeningTime,
            boolean isValid
    ) throws Exception {
        //  given
        User user = someBusinessUserModel1();

        when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));

        //  when
        //  then
        if (isValid) {
            when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(Optional.of(foundedRestaurant));
            when(restaurantMapper.mapScheduleDataToRestaurant(
                    updatedOpeningTime.getOpeningHour(),
                    updatedOpeningTime.getCloseHour(),
                    updatedOpeningTime.getDayOfWeekFrom(),
                    updatedOpeningTime.getDayOfWeekTill(),
                    foundedRestaurant
            )).thenReturn(updatedRestaurant);

            mockMvc.perform(patch(BASE_PATH + "/restaurant/{restaurantEmail}/update-schedule", restaurantEmail)
                            .param("openingHour", updatedOpeningTime.getOpeningHour())
                            .param("closeHour", updatedOpeningTime.getCloseHour())
                            .param("dayOfWeekFrom", updatedOpeningTime.getDayOfWeekFrom().name())
                            .param("dayOfWeekTill", updatedOpeningTime.getDayOfWeekTill().name())
                    )
                    .andExpect(content().string("Restaurant schedule updated successfully"))
                    .andExpect(status().isOk());

            ArgumentCaptor<RestaurantEntity> restaurantEntityArgumentCaptor = ArgumentCaptor.forClass(RestaurantEntity.class);
            ArgumentCaptor<Restaurant> restaurantArgumentCaptor = ArgumentCaptor.forClass(Restaurant.class);

            verify(restaurantService, times(1)).updateRestaurantSchedule(restaurantEntityArgumentCaptor.capture(), restaurantArgumentCaptor.capture());
        } else {
            mockMvc.perform(patch(BASE_PATH + "/restaurant/{restaurantEmail}/update-schedule", restaurantEmail)
                            .param("openingHour", updatedOpeningTime.getOpeningHour())
                            .param("closeHour", updatedOpeningTime.getCloseHour())
                            .param("dayOfWeekFrom", updatedOpeningTime.getDayOfWeekFrom().name())
                            .param("dayOfWeekTill", updatedOpeningTime.getDayOfWeekTill().name())
                    )
                    .andExpect(status().isNotFound());

            verify(restaurantService, never()).updateRestaurantSchedule(foundedRestaurant, updatedRestaurant);
        }
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForRestaurantImageToUpdate")
    void thatShouldUpdateRestaurantImageCorrectly(
            String restaurantEmail,
            RestaurantEntity foundedRestaurant,
            RestaurantImageType restaurantImageType,
            MockMultipartFile restaurantImage,
            boolean isValid
    ) throws Exception {
        //  given
        User user = someBusinessUserModel1();

        when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));

        //  when
        //  then
        if (isValid) {
            when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(Optional.of(foundedRestaurant));

            mockMvc.perform(multipart(BASE_PATH + "/restaurant/{restaurantEmail}/update-restaurant-image", restaurantEmail)
                            .file(restaurantImage)
                            .param("restaurantImageType", restaurantImageType.name())
                            .with(request -> {
                                request.setMethod("PATCH");
                                return request;
                            })
                    )
                    .andExpect(content().string("Restaurant image: [%s] updated successfully".formatted(restaurantImageType.name())))
                    .andExpect(status().isOk());
        } else {
            mockMvc.perform(multipart(BASE_PATH + "/restaurant/{restaurantEmail}/update-restaurant-image", restaurantEmail)
                            .file(restaurantImage)
                            .param("restaurantImageType", restaurantImageType.name())
                            .with(request -> {
                                request.setMethod("PATCH");
                                return request;
                            })
                    )
                    .andExpect(status().isNotFound());
        }
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForFoodToDelete")
    void thatShouldDeleteFoodFromMenuCorrectly(
            String restaurantEmail,
            RestaurantEntity restaurantEntity,
            String foodId,
            boolean isValid
    ) throws Exception {
        //  given
        User user = someBusinessUserModel1();
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantEntity != null ? Optional.of(restaurantEntity) : Optional.empty();

        when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));
        when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(optionalRestaurantEntity);

        //  when
        //  then
        if (isValid) {
            mockMvc.perform(patch(BASE_PATH + "/restaurant/{restaurantEmail}/delete-food-from-menu", restaurantEmail)
                            .param("foodId", foodId))
                    .andExpect(content().string("Food with id: [%s] delete successfully".formatted(foodId)))
                    .andExpect(status().isOk());

            verify(foodMenuService, times(1)).deleteFoodFromMenu(Integer.parseInt(foodId), restaurantEmail);
        } else {
            mockMvc.perform(patch(BASE_PATH + "/restaurant/{restaurantEmail}/delete-food-from-menu", restaurantEmail)
                            .param("foodId", foodId))
                    .andExpect(status().isNotFound());

            verify(foodMenuService, never()).deleteFoodFromMenu(Integer.parseInt(foodId), restaurantEmail);
        }
    }

    private static Stream<Arguments> provideArgumentsForFoodToDelete() {
        FoodMenuEntity foodMenuEntity = someFoodMenuEntity1();
        RestaurantEntity restaurantEntity = someRestaurantEntity1();
        restaurantEntity.setFoodMenu(foodMenuEntity);

        return Stream.of(
                Arguments.of("na_wypasie@restaurant.pl", restaurantEntity, "1", true),
                Arguments.of("na_wypasie@restaurant.pl", restaurantEntity, "5", false),
                Arguments.of("example@restaurant.pl", null, "5", false)
        );
    }

    private static Stream<Arguments> provideArgumentsForRestaurantImageToUpdate() {
        MockMultipartFile updatedHeaderImage = new MockMultipartFile("restaurantImage", "na_wypasie_header_image.jpg", "image/jpeg", "some-image-content".getBytes());
        MockMultipartFile updatedCardImage = new MockMultipartFile("restaurantImage", "na_wypasie_card_image.jpg", "image/jpeg", "some-image-content".getBytes());

        return Stream.of(
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), RestaurantImageType.HEADER, updatedHeaderImage, true),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), RestaurantImageType.CARD, updatedCardImage, true),
                Arguments.of("na_wypasie@restaurant.pl", null, RestaurantImageType.CARD, updatedCardImage, false)
        );
    }

    private static Stream<Arguments> provideArgumentsForRestaurantSchedule() {
        Restaurant updatedRestaurant = someRestaurantModel1();
        RestaurantOpeningTime updatedOpeningTime = someRestaurantOpeningTimeModel2();

        updatedRestaurant.setRestaurantOpeningTime(updatedOpeningTime);

        return Stream.of(
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), updatedRestaurant, updatedOpeningTime, true),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), updatedRestaurant, someRestaurantOpeningTimeModel3(), false),
                Arguments.of("example@restaurant.pl", null, updatedRestaurant, updatedOpeningTime, false)
        );
    }

    private static Stream<Arguments> provideArgumentsForFoodToCreate() {
        FoodMenuEntity foodMenuEntity = someFoodMenuEntity1();
        RestaurantEntity restaurantEntity = someRestaurantEntity1();
        restaurantEntity.setFoodMenu(foodMenuEntity);

        return Stream.of(
                Arguments.of("na_wypasie@restaurant.pl", restaurantEntity, someFoodRequestDTO1(), someFoodModel5(), true),
                Arguments.of("na_wypasie@restaurant.pl", restaurantEntity, someFoodRequestDTO1(), null, false),
                Arguments.of("example@restaurant.pl", null, someFoodRequestDTO1(), someFoodModel5(), false)
        );
    }

    private static Stream<Arguments> provideArgumentsForRestaurant() {
        return Stream.of(
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szybko", null, null, null, null, null, null, true),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szy2bko3", null, null, null, null, null, null, false),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szybko", "214281647", null, null, null, null, null, true),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szybko", "214281647110", null, null, null, null, null, false),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szybko", "21q2816az", null, null, null, null, null, false),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szybko", "214281647", "na_szybko@restaurant.pl", null, null, null, null, true),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szybko", "214281647", "na_szybko_restaurant.pl", null, null, null, null, false),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szybko", "214281647", "na_szybko@restaurant.pl", "Warszawa", null, null, null, true),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szybko", "214281647", "na_szybko@restaurant.pl", "Warszawa101", null, null, null, false),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szybko", "214281647", "na_szybko@restaurant.pl", "Warszawa", "Wola", null, null, true),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szybko", "214281647", "na_szybko@restaurant.pl", "Warszawa", "W01a", null, null, false),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szybko", "214281647", "na_szybko@restaurant.pl", "Warszawa", "Wola", "21-110", null, true),
                Arguments.of("na_wypasie@restaurant.pl", someRestaurantEntity1(), "Na Szybko", "214281647", "na_szybko@restaurant.pl", "Warszawa", "Wola", "21-110", "Kręta 21", true),
                Arguments.of("na_wypasie@example.pl", null, "Na Szybko", "2142816473", "na_szybko@restaurant.pl", "Warszawa", "Wola", "21-110", "Kręta 21", false)
        );
    }

    private static Stream<Arguments> provideArgumentsForOwnerProfile() {
        return Stream.of(
                Arguments.of(null, null, null, "Nowak", null, null),
                Arguments.of(null, null, "Jan", "Nowak", null, null),
                Arguments.of(null, "jan_nowak@business_user.com.pl", "Jan", "Nowak", null, null),
                Arguments.of("jan_nowak_91", "jan_nowak@business_user.com.pl", "Jan", "Nowak", null, null),
                Arguments.of("jan_nowak_91", "jan_nowak@business_user.com.pl", "Jan", "Nowak", null, "91014488899"),
                Arguments.of("jan_nowak_91", "jan_nowak@business_user.com.pl", "Jan", "Nowak", "noweHaslo", "91014488899")
        );
    }

    private static Optional<Restaurant> findOptionalRestaurantByEmailFromUserOwner(String email, User user) {
        return user.getOwner().getRestaurants().stream()
                .filter(restaurant -> restaurant.getRestaurantEmail().equals(email))
                .findFirst();
    }

    private static void setOrderNumberToRestaurantAndDTO(
            User user,
            RestaurantDTO restaurantDTO,
            String restaurantEmail,
            Integer orderNumber,
            boolean isDataFound
    ) {
        Optional<Restaurant> optionalRestaurant = findOptionalRestaurantByEmailFromUserOwner(restaurantEmail, user);

        optionalRestaurant.ifPresent(restaurant -> {
            if (isDataFound) {
                restaurant.setCustomerOrdersNumbers(List.of(orderNumber));
                restaurantDTO.setCustomerOrdersNumbers(List.of(orderNumber));
            } else {
                restaurant.setCustomerOrdersNumbers(List.of());
            }
        });
    }

    private static Stream<Arguments> provideListOfRestaurantEmails() {
        return Stream.of(
                Arguments.of("na_wypasie@restaurant.pl", "12345", true),
                Arguments.of("wypasie@restaurant.pl", "21321", false),
                Arguments.of("zapiecek@restaurant.pl", "54321", true),
                Arguments.of("na_wypasie@zapiecek.co.uk", "", false)
        );
    }


    private static Stream<Arguments> provideData() {
        return Stream.of(
                Arguments.of("na_wypasie@restaurant.pl", "12345", someOrdersModel1(), someOrderDTO1(), someRestaurantDTO1(), true),
                Arguments.of("zapiecek@restaurant.pl", "54321", someOrdersModel2(), someOrderDTO2(), someRestaurantDTO2(), true),
                Arguments.of("zapiecek@restaurant.pl", "12231", null, null, someRestaurantDTO2(), false),
                Arguments.of("wypasie@restaurant.pl", "12345", someOrdersModel1(), someOrderDTO1(), null, false)
        );
    }
}