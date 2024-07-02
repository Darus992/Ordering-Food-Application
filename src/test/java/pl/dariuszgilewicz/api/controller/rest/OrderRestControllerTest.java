package pl.dariuszgilewicz.api.controller.rest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.dariuszgilewicz.business.OrderService;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodRepository;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dariuszgilewicz.util.FoodFixtures.*;
import static pl.dariuszgilewicz.util.UsersFixtures.someBusinessUserModel1;
import static pl.dariuszgilewicz.util.UsersFixtures.someCustomerUserModel1;

@WebMvcTest(controllers = OrderRestController.class)
@Import(SecurityConfiguration.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private OrderService orderService;
    @MockBean
    private FoodRepository foodRepository;

    public static final String BASE_PATH = "/api/order/create-order";

    @ParameterizedTest
    @MethodSource("provideData")
    void thatCreateOrderShouldWorkCorrectly(
            Integer[] foodsId,
            Integer[] foodsValues,
            List<FoodEntity> foodEntityList,
            BigDecimal totalPrice,
            String orderNotes,
            String restaurantEmail,
            boolean isValid
    ) throws Exception {
        //  given
        User user = someCustomerUserModel1();

        when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));

        if (isValid) {
            for (FoodEntity foodEntity : foodEntityList) {
                when(foodRepository.findFoodEntityById(foodEntity.getFoodId())).thenReturn(foodEntity);
            }

            //  when
            //  then
            mockMvc.perform(post(BASE_PATH)
                            .params(buildParams("foodsId", foodsId))
                            .params(buildParams("foodsValues", foodsValues))
                            .param("orderNotes", orderNotes)
                            .param("restaurantEmail", restaurantEmail)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Order created successfully"));

            verify(orderService, times(1)).createOrderAndReturnOrderNumber(
                    eq(foodsId),
                    eq(foodsValues),
                    eq(totalPrice),
                    eq(orderNotes),
                    eq(user),
                    eq(restaurantEmail)
            );
        } else {
            mockMvc.perform(post(BASE_PATH))
                    .andExpect(status().is5xxServerError());
        }
    }

    @ParameterizedTest
    @MethodSource("provideUserData")
    void thatCreateOrderShouldWorkCorrectlyOnlyWithAuthenticatedCustomer(User user, boolean isCustomer) throws Exception {
        //  given
        Integer[] foodsId = {4, 2, 3};
        Integer[] foodsValues = {1, 2, 3};
        BigDecimal totalPrice = BigDecimal.valueOf(153.5);
        String restaurantEmail = "nazwaRestauracji@food.pl";
        List<FoodEntity> foodEntities = someFoodEntityList6();


        for (FoodEntity foodEntity : foodEntities) {
            when(foodRepository.findFoodEntityById(foodEntity.getFoodId())).thenReturn(foodEntity);
        }

        //  when
        //  then
        if (isCustomer && user != null) {

            when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));

            mockMvc.perform(post(BASE_PATH)
                            .params(buildParams("foodsId", foodsId))
                            .params(buildParams("foodsValues", foodsValues))
                            .param("orderNotes", "")
                            .param("restaurantEmail", restaurantEmail))
                    .andExpect(status().isOk());

            verify(orderService, times(1)).createOrderAndReturnOrderNumber(
                    foodsId,
                    foodsValues,
                    totalPrice,
                    "",
                    user,
                    restaurantEmail
            );

        } else {
            mockMvc.perform(post(BASE_PATH)
                            .params(buildParams("foodsId", foodsId))
                            .params(buildParams("foodsValues", foodsValues))
                            .param("orderNotes", "")
                            .param("restaurantEmail", restaurantEmail))
                    .andExpect(status().isUnauthorized());
        }
    }

    private static Stream<Arguments> provideData() {
        Integer[] foodsId1 = {4, 2, 3};
        Integer[] foodsId2 = {3, 1};
        Integer[] foodsId3 = {4, 3};
        Integer[] foodsId4 = {4, 2, 3};

        Integer[] foodsValues1 = {1, 2, 3};
        Integer[] foodsValues2 = {2, 3};
        Integer[] foodsValues3 = {1, 1};
        Integer[] foodsValues4 = {1, 2};
        String restaurantEmail = "nazwaRestauracji@food.pl";

        List<FoodEntity> foodList1 = someFoodEntityList6();
        List<FoodEntity> foodList2 = someFoodEntityList3();
        List<FoodEntity> foodList3 = someFoodEntityList1();

        return Stream.of(
                Arguments.of(foodsId1, foodsValues1, foodList1, BigDecimal.valueOf(153.5), null, restaurantEmail, true),
                Arguments.of(foodsId2, foodsValues2, foodList2, BigDecimal.valueOf(30), "Entrance is from the other side of the road", restaurantEmail, true),
                Arguments.of(foodsId3, foodsValues3, foodList3, BigDecimal.valueOf(41.5), "Doorbell doesn't work", restaurantEmail, true),
                Arguments.of(foodsId4, foodsValues4, foodList1, BigDecimal.valueOf(141.5), null, restaurantEmail, false)
        );
    }

    private static Stream<Arguments> provideUserData() {
        return Stream.of(
                Arguments.of(someCustomerUserModel1(), true),
                Arguments.of(someBusinessUserModel1(), false),
                Arguments.of(null, false)
        );
    }

    private MultiValueMap<String, String> buildParams(String paramName, Integer[] values) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        for (Integer value : values) {
            params.add(paramName, value.toString());
        }
        return params;
    }
}