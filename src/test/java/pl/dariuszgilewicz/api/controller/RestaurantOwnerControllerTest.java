package pl.dariuszgilewicz.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import pl.dariuszgilewicz.business.FoodMenuService;
import pl.dariuszgilewicz.business.OrderService;
import pl.dariuszgilewicz.business.RestaurantService;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserRole;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dariuszgilewicz.util.FoodFixtures.someFoodModel1;
import static pl.dariuszgilewicz.util.FoodFixtures.someFoodModel2;
import static pl.dariuszgilewicz.util.OrdersFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someRestaurantEntity1;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someRestaurantModel1;
import static pl.dariuszgilewicz.util.UsersFixtures.someBusinessUserModel1;
import static pl.dariuszgilewicz.util.UsersFixtures.someBusinessUserModel2;

@WebMvcTest(RestaurantOwnerController.class)
@Import(SecurityConfiguration.class)
class RestaurantOwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private RestaurantJpaRepository restaurantJpaRepository;
    @MockBean
    private FoodJpaRepository foodJpaRepository;
    @MockBean
    private FoodMenuService foodMenuService;
    @MockBean
    private RestaurantEntityMapper restaurantEntityMapper;
    @MockBean
    private OrderService orderService;
    @MockBean
    private OrderRepository orderRepository;


    //  showOwnerPage()
    @Test
    @WithMockUser(username = "business_user2", authorities = {"OWNER"})
    void showOwnerPage_shouldWorkCorrectlyWhenUserIsOwner200() throws Exception {
        //  given
        MockHttpSession session = new MockHttpSession();
        User user = someBusinessUserModel2();
        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(user));
        session.setAttribute("restaurants", user.getOwner().getRestaurants());

        //  when
        //  then
        mockMvc.perform(get("/owner")
                        .flashAttr("isAuthenticated", true)
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("owner"))
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(containsString("Your account")))
                .andExpect(content().string(containsString("Create New Restaurant")))
                .andExpect(content().string(containsString("Update Profile")))
                .andExpect(model().attribute("restaurants", user.getOwner().getRestaurants()))
                .andExpect(model().attribute("isAuthenticated", true));
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void showOwnerPage_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(get("/owner"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void showOwnerPage_shouldFailWhenIsNoLogin302() throws Exception {
        //  given
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(get("/owner"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }


    //  showOwnerUpdatePage()
    @Test
    @WithMockUser(username = "business_user2", authorities = {"OWNER"})
    void showOwnerUpdatePage_shouldWorkCorrectlyWhenUserIsOwner200() throws Exception {
        //  given
        User currentUser = someBusinessUserModel2();
        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(currentUser));

        //  when
        //  then
        mockMvc.perform(get("/owner/update-profile")
                        .flashAttr("isAuthenticated", true)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("owner_update_data_form"))
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(containsString("Your account")))
                .andExpect(content().string(containsString("Account Details")))
                .andExpect(content().string(containsString("Username:")))
                .andExpect(content().string(containsString("Email:")))
                .andExpect(content().string(containsString("Password:")))
                .andExpect(content().string(containsString("Name:")))
                .andExpect(content().string(containsString("Surname:")))
                .andExpect(content().string(containsString("Pesel:")))
                .andExpect(content().string(containsString("Save change")))
                .andExpect(content().string(containsString("Do you want to delete your account?")))
                .andExpect(content().string(containsString("Delete my account")))
                .andExpect(model().attributeExists("userForm"))
                .andExpect(model().attribute("user", currentUser));
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void showOwnerUpdatePage_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(get("/owner/update-profile"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void showOwnerUpdatePage_shouldFailWhenIsNoLogin302() throws Exception {
        //  given
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(get("/owner/update-profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }


    //  showRestaurantDetailsForEditePage()
    //  showRestaurantCreateFoodPage()
    //  showRestaurantDeleteFoodFromMenuPage()
    //  showRestaurantEditFoodFromMenuPage()
    //  showRestaurantUpdateImagePage()
    //  showUpdateSchedulePage()
    //  showRestaurantOrdersPage()
    @ParameterizedTest
    @ValueSource(strings = {
            "/owner/restaurant/{restaurantEmail}/details",
            "/owner/restaurant/{restaurantEmail}/create-food",
            "/owner/restaurant/{restaurantEmail}/delete-food-from-menu",
            "/owner/restaurant/{restaurantEmail}/edite-food-from-menu",
            "/owner/restaurant/{restaurantEmail}/update-restaurant-image",
            "/owner/restaurant/{restaurantEmail}/update-schedule",
            "/owner/restaurant/{restaurantEmail}/order-update"
    })
    @WithMockUser(username = "business_user", authorities = {"OWNER"})
    void shouldDisplayRestaurantPageForOwnerWhenAuthenticated(String path) throws Exception {
        //  given
        MockHttpSession session = new MockHttpSession();
        User currentUser = someBusinessUserModel1();
        String restaurantEmail = "na_wypasie@restaurant.pl";
        session.setAttribute("user", currentUser);
        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(currentUser));

        //  when
        //  then
        mockMvc.perform(get(path, restaurantEmail)
                        .flashAttr("isAuthenticated", true)
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("owner_restaurant_details_edit"))
                .andExpect(model().attributeExists("restaurant"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("food"))
                .andExpect(model().attribute("isAuthenticated", true));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/owner/restaurant/{restaurantEmail}/details",
            "/owner/restaurant/{restaurantEmail}/create-food",
            "/owner/restaurant/{restaurantEmail}/delete-food-from-menu",
            "/owner/restaurant/{restaurantEmail}/edite-food-from-menu",
            "/owner/restaurant/{restaurantEmail}/update-restaurant-image",
            "/owner/restaurant/{restaurantEmail}/update-schedule",
            "/owner/restaurant/{restaurantEmail}/order-update"
    })
    @WithMockUser(username = "business_user2", authorities = {"OWNER"})
    void shouldDisplayRestaurantPageForOwnerWhenAuthenticatedWithOrders(String path) throws Exception {
        //  given
        MockHttpSession session = new MockHttpSession();
        User currentUser = someBusinessUserModel2();
        List<Integer> someCustomerOrderNumbers = someCustomerOrderNumbers1();
        List<Orders> ordersList = someOrdersModelList1();
        String restaurantEmail = "karczma@restauracja.pl";
        session.setAttribute("user", currentUser);
        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(currentUser));
        when(restaurantService.createOrdersListByOrderNumber(someCustomerOrderNumbers)).thenReturn(ordersList);

        //  when
        //  then
        mockMvc.perform(get(path, restaurantEmail)
                        .flashAttr("isAuthenticated", true)
                        .session(session)
                )
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(containsString("Your account")))
                .andExpect(content().string(containsString("Details")))
                .andExpect(content().string(containsString("Food Form")))
                .andExpect(content().string(containsString("Food Menu")))
                .andExpect(content().string(containsString("Restaurant Image")))
                .andExpect(content().string(containsString("Work Schedule")))
                .andExpect(content().string(containsString("Orders List")))
                .andExpect(content().string(containsString("Restaurant Name:")))
                .andExpect(content().string(containsString("Restaurant Phone:")))
                .andExpect(content().string(containsString("Restaurant Email:")))
                .andExpect(content().string(containsString("Restaurant City:")))
                .andExpect(content().string(containsString("Restaurant Street:")))
                .andExpect(content().string(containsString("District:")))
                .andExpect(content().string(containsString("Postal Code:")))
                .andExpect(content().string(containsString("Update Restaurant Details")))
                .andExpect(content().string(containsString("Category")))
                .andExpect(content().string(containsString("Name")))
                .andExpect(content().string(containsString("Description")))
                .andExpect(content().string(containsString("Price")))
                .andExpect(content().string(containsString("Image")))
                .andExpect(content().string(containsString("Create")))
                .andExpect(content().string(containsString("Edit")))
                .andExpect(content().string(containsString("Current Image")))
                .andExpect(content().string(containsString("Update")))
                .andExpect(content().string(containsString("Delete")))
                .andExpect(content().string(containsString("Header Image")))
                .andExpect(content().string(containsString("Update Header Image")))
                .andExpect(content().string(containsString("Card Image")))
                .andExpect(content().string(containsString("Update Card Image")))
                .andExpect(content().string(containsString("Work Schedule")))
                .andExpect(content().string(containsString("Current Time:")))
                .andExpect(content().string(containsString("Opening Hour:")))
                .andExpect(content().string(containsString("Closing Hour:")))
                .andExpect(content().string(containsString("Current Working Days:")))
                .andExpect(content().string(containsString("Starting Day:")))
                .andExpect(content().string(containsString("Finishing Day:")))
                .andExpect(content().string(containsString("Update Schedule")))
                .andExpect(content().string(containsString("Order Status:")))
                .andExpect(content().string(containsString("Order Number:")))
                .andExpect(content().string(containsString("Change order status:")))
                .andExpect(content().string(containsString("Change")))
                .andExpect(content().string(containsString("Received Date Time:")))
                .andExpect(content().string(containsString("Customer:")))
                .andExpect(content().string(containsString("Phone:")))
                .andExpect(content().string(containsString("Details")))
                .andExpect(content().string(not(containsString("value=\"11221\""))));

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/owner/restaurant/{restaurantEmail}/details",
            "/owner/restaurant/{restaurantEmail}/create-food",
            "/owner/restaurant/{restaurantEmail}/delete-food-from-menu",
            "/owner/restaurant/{restaurantEmail}/edite-food-from-menu",
            "/owner/restaurant/{restaurantEmail}/update-restaurant-image",
            "/owner/restaurant/{restaurantEmail}/update-schedule",
            "/owner/restaurant/{restaurantEmail}/order-update"
    })
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void shouldNotDisplayRestaurantPageForOwnerWhenUserIsCustomer(String path) throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(get(path, restaurantEmail))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/owner/restaurant/{restaurantEmail}/details",
            "/owner/restaurant/{restaurantEmail}/create-food",
            "/owner/restaurant/{restaurantEmail}/delete-food-from-menu",
            "/owner/restaurant/{restaurantEmail}/edite-food-from-menu",
            "/owner/restaurant/{restaurantEmail}/update-restaurant-image",
            "/owner/restaurant/{restaurantEmail}/update-schedule",
            "/owner/restaurant/{restaurantEmail}/order-update"
    })
    @WithAnonymousUser
    void shouldRedirectFromRestaurantPageForOwnerWhenUserIsNotLogin(String path) throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(get(path, restaurantEmail))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }


    //  showRestaurantOrderDetailsPage()
    @Test
    @WithMockUser(username = "business_user2", authorities = {"OWNER"})
    void showRestaurantOrderDetailsPage_shouldWorkCorrectlyWhenUserIsOwner200() throws Exception {
        //  given
        MockHttpSession session = new MockHttpSession();
        User user = someBusinessUserModel2();
        Orders expectedOrder = someOrdersModel1();

        String restaurantEmail = "kociolek@restaurant.pl";
        String orderNumber = "12345";

        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(user));
        when(orderRepository.findOrderByOrderNumber(Integer.parseInt(orderNumber))).thenReturn(expectedOrder);
        session.setAttribute("order", expectedOrder);

        //  when
        //  then
        mockMvc.perform(get("/owner/restaurant/{restaurantEmail}/order-details/{orderNumber}", restaurantEmail, orderNumber)
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("order_details"))
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(containsString("Your account")))
                .andExpect(content().string(containsString("Order Number:")))
                .andExpect(content().string(containsString(orderNumber)))
                .andExpect(content().string(containsString("Order Status:")))
                .andExpect(content().string(containsString(expectedOrder.getStatus().name())))
                .andExpect(content().string(containsString("Restaurant Address:")))
                .andExpect(content().string(containsString(expectedOrder.getRestaurant().getRestaurantAddress().getAddressStreet())))
                .andExpect(content().string(containsString(expectedOrder.getRestaurant().getRestaurantAddress().getPostalCode())))
                .andExpect(content().string(containsString(expectedOrder.getRestaurant().getRestaurantAddress().getCity())))
                .andExpect(content().string(containsString("Restaurant Name:")))
                .andExpect(content().string(containsString(expectedOrder.getRestaurant().getRestaurantName())))
                .andExpect(content().string(containsString("Restaurant Phone:")))
                .andExpect(content().string(containsString(expectedOrder.getRestaurant().getRestaurantPhone())))
                .andExpect(content().string(containsString("Restaurant Email:")))
                .andExpect(content().string(containsString(expectedOrder.getRestaurant().getRestaurantEmail())))
                .andExpect(content().string(containsString("Received Date Time:")))
                .andExpect(content().string(containsString(expectedOrder.getReceivedDateTime())))
                .andExpect(content().string(containsString("Delivery Address:")))
                .andExpect(content().string(containsString(expectedOrder.getCustomer().getAddress().getAddressStreet())))
                .andExpect(content().string(containsString(expectedOrder.getCustomer().getAddress().getPostalCode())))
                .andExpect(content().string(containsString(expectedOrder.getCustomer().getAddress().getCity())))
                .andExpect(content().string(containsString("Customer Name:")))
                .andExpect(content().string(containsString(expectedOrder.getCustomer().getName())))
                .andExpect(content().string(containsString("Customer Surname:")))
                .andExpect(content().string(containsString(expectedOrder.getCustomer().getSurname())))
                .andExpect(content().string(containsString("Customer Phone:")))
                .andExpect(content().string(containsString(expectedOrder.getCustomer().getPhone())))
                .andExpect(content().string(containsString("Completed Date Time:")))
                .andExpect(content().string(containsString("Total Price:")))
                .andExpect(content().string(containsString(expectedOrder.getTotalPrice().toString())))
                .andExpect(content().string(containsString("Order Notes:")))
                .andExpect(content().string(containsString(expectedOrder.getOrderNotes())))
                .andExpect(model().attribute("order", expectedOrder));
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void showRestaurantOrderDetailsPage_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String restaurantEmail = "kociolek@restaurant.pl";
        String orderNumber = "12345";

        //  when
        //  then
        mockMvc.perform(get("/owner/restaurant/{restaurantEmail}/order-details/{orderNumber}", restaurantEmail, orderNumber))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void showRestaurantOrderDetailsPage_shouldFailWhenIsNotLogin302() throws Exception {
        //  given
        String restaurantEmail = "kociolek@restaurant.pl";
        String orderNumber = "12345";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(get("/owner/restaurant/{restaurantEmail}/order-details/{orderNumber}", restaurantEmail, orderNumber))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }


    //  updateRestaurantOrder()
    @ParameterizedTest
    @ValueSource(strings = {"ON_THE_WAY", "COMPLETED"})
    @WithMockUser(username = "business_user2", authorities = {"OWNER"})
    void updateRestaurantOrder_shouldWorkCorrectlyWhenUserIsOwner(String statusChanger) throws Exception {
        //  given
        User user = someBusinessUserModel2();
        String restaurantEmail = "kociolek@restaurant.pl";
        Integer orderNumber = 12345;
        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(user));

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/order-update", restaurantEmail)
                        .param("statusChanger", statusChanger)
                        .param("orderNumber", String.valueOf(orderNumber))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/owner"));

        verify(orderService, times(1)).updateOrderStatus(orderNumber, statusChanger);
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void updateRestaurantOrder_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String restaurantEmail = "kociolek@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/order-update", restaurantEmail))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void updateRestaurantOrder_shouldFailWhenIsNotLogin302() throws Exception {
        //  given
        String restaurantEmail = "kociolek@restaurant.pl";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/order-update", restaurantEmail))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }


    //  updateOwnerProfile()
    @ParameterizedTest
    @MethodSource("provideTestData")
    @WithMockUser(username = "business_user2", authorities = {"OWNER"})
    void updateOwnerProfile_shouldWorkCorrectlyDependingOnValidationWhenUserIsOwner(
            User expectedUser,
            String emailParam,
            List<String> errorMessages,
            boolean hasErrors
    ) throws Exception {
        //  given
        User currentUser = someBusinessUserModel2();
        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(currentUser));

        //  when
        //  then
        if (hasErrors) {
            ResultActions resultActions = mockMvc.perform(patch("/owner/update-profile")
                            .flashAttr("userForm", expectedUser)
                            .param("emailParam", emailParam)
                    )
                    .andExpect(view().name("owner_update_data_form"));

            for (String errorMessage : errorMessages) {
                resultActions.andExpect(content().string(containsString(errorMessage)));
            }

            verify(userService, never()).updateUserData(expectedUser, emailParam);
        } else {
            mockMvc.perform(patch("/owner/update-profile")
                            .flashAttr("userForm", expectedUser)
                            .param("emailParam", emailParam)
                    )
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/owner"));

            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<String> userEmailCaptor = ArgumentCaptor.forClass(String.class);
            verify(userService, times(1)).updateUserData(userArgumentCaptor.capture(), userEmailCaptor.capture());

            assertEquals(expectedUser, userArgumentCaptor.getValue());
            assertEquals(emailParam, userEmailCaptor.getValue());
        }
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void updateOwnerProfile_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(patch("/owner/update-profile"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void updateOwnerProfile_shouldFailWhenIsNotLogin302() throws Exception {
        //  given
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(patch("/owner/update-profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }


    //  updateRestaurantDetails()
    @Test
    @WithMockUser(username = "business_user2", authorities = {"OWNER"})
    void updateRestaurantDetails_shouldWorkCorrectlyWhenUserIsOwner() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        RestaurantEntity expectedEntityRestaurant = someRestaurantEntity1();
        Restaurant expectedUpdatedRestaurantModel = someRestaurantModel1();
        expectedUpdatedRestaurantModel.setRestaurantPhone("1114448889");

        when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(Optional.of(expectedEntityRestaurant));

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/details", restaurantEmail)
                        .flashAttr("restaurant", expectedUpdatedRestaurantModel)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/owner"));

        ArgumentCaptor<RestaurantEntity> restaurantEntityCaptor = ArgumentCaptor.forClass(RestaurantEntity.class);
        ArgumentCaptor<Restaurant> restaurantModelCaptor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantService, times(1)).updateRestaurantDetails(restaurantEntityCaptor.capture(), restaurantModelCaptor.capture());

        assertEquals(expectedEntityRestaurant, restaurantEntityCaptor.getValue());
        assertEquals(expectedUpdatedRestaurantModel, restaurantModelCaptor.getValue());
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void updateRestaurantDetails_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/details", restaurantEmail))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void updateRestaurantDetails_shouldFailWhenIsNotLogin302() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/details", restaurantEmail))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }


    //  deleteFoodFromMenu()
    @Test
    @WithMockUser(username = "business_user2", authorities = {"OWNER"})
    void deleteFoodFromMenu_shouldWorkCorrectlyWhenUserIsOwner() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        Integer foodId = 1;

        //  when
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/delete-food-from-menu", restaurantEmail)
                        .param("foodId", String.valueOf(foodId))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/owner"));

        //  then
        ArgumentCaptor<Integer> foodIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> restaurantEmailCaptor = ArgumentCaptor.forClass(String.class);
        verify(foodMenuService, times(1)).deleteFoodFromMenu(foodIdCaptor.capture(), restaurantEmailCaptor.capture());

        assertEquals(foodId, foodIdCaptor.getValue());
        assertEquals(restaurantEmail, restaurantEmailCaptor.getValue());
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void deleteFoodFromMenu_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/delete-food-from-menu", restaurantEmail))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void deleteFoodFromMenu_shouldFailWhenIsNotLogin302() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/delete-food-from-menu", restaurantEmail))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }


    //  editFoodFromMenu()
    @ParameterizedTest
    @MethodSource("provideTestDataForFood")
    @WithMockUser(username = "business_user2", authorities = {"OWNER"})
    void editFoodFromMenu_shouldWorkCorrectlyDependingOnValidationWhenUserIsOwner(
            Food expectedFoodModel,
            boolean hasError,
            String errorMessage
    ) throws Exception {

        //  given
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity1();
        Restaurant expectedRestaurantModel = someRestaurantModel1();
        String restaurantEmail = "na_wypasie@restaurant.pl";

        when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(Optional.of(expectedRestaurantEntity));
        when(restaurantEntityMapper.mapFromEntity(expectedRestaurantEntity)).thenReturn(expectedRestaurantModel);

        //  when
        BindingResult bindingResult = createBindingResult(hasError, errorMessage);

        //  then
        if (hasError) {
            mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/edite-food-from-menu", restaurantEmail)
                            .flashAttr("food", expectedFoodModel)
                            .flashAttr(BindingResult.MODEL_KEY_PREFIX + "food", bindingResult)
                    )
                    .andExpect(view().name("owner_restaurant_details_edit"))
                    .andExpect(model().attributeExists("restaurant"))
                    .andExpect(model().attributeExists("defaultTab"))
                    .andExpect(content().string(containsString(errorMessage)));

            verify(foodMenuService, never()).editFoodFromMenu(any());
        } else {
            mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/edite-food-from-menu", restaurantEmail)
                            .flashAttr("food", expectedFoodModel)
                            .flashAttr(BindingResult.MODEL_KEY_PREFIX + "food", bindingResult)
                    )
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/owner"));

            ArgumentCaptor<Food> captor = ArgumentCaptor.forClass(Food.class);
            verify(foodMenuService, times(1)).editFoodFromMenu(captor.capture());

            Food capturedFood = captor.getValue();
            assertEquals(expectedFoodModel, capturedFood);
        }
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void editFoodFromMenu_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/edite-food-from-menu", restaurantEmail))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void editFoodFromMenu_shouldFailWhenIsNotLogin302() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/edite-food-from-menu", restaurantEmail))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }


    //  updateRestaurantImage()
    @ParameterizedTest
    @MethodSource("provideTestDataForRestaurantImage")
    @WithMockUser(username = "business_user2", authorities = {"OWNER"})
    void updateRestaurantImage_shouldWorkCorrectlyWhenUserIsOwner(MultipartFile expectedMultipartFile, String expectedImageParam) throws Exception {
        //  given
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity1();
        String restaurantEmail = "na_wypasie@restaurant.pl";

        when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(Optional.of(expectedRestaurantEntity));

        //  when
        mockMvc.perform(multipart("/owner/restaurant/{restaurantEmail}/update-restaurant-image", restaurantEmail)
                        .file((MockMultipartFile) expectedMultipartFile)
                        .param("image", expectedImageParam)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/owner"));

        //  then
        if (!expectedMultipartFile.isEmpty()) {
            ArgumentCaptor<RestaurantEntity> restaurantEntityArgumentCaptor = ArgumentCaptor.forClass(RestaurantEntity.class);
            ArgumentCaptor<MultipartFile> multipartFileArgumentCaptor = ArgumentCaptor.forClass(MultipartFile.class);
            ArgumentCaptor<String> imageParamArgumentCaptor = ArgumentCaptor.forClass(String.class);

            verify(restaurantService, times(1)).updateRestaurantImage(
                    multipartFileArgumentCaptor.capture(),
                    restaurantEntityArgumentCaptor.capture(),
                    imageParamArgumentCaptor.capture()
            );

            MultipartFile multipartFileArgumentCaptorValue = multipartFileArgumentCaptor.getValue();
            RestaurantEntity restaurantEntityArgumentCaptorValue = restaurantEntityArgumentCaptor.getValue();
            String imageParamArgumentCaptorValue = imageParamArgumentCaptor.getValue();

            assertEquals(expectedMultipartFile, multipartFileArgumentCaptorValue);
            assertEquals(expectedRestaurantEntity, restaurantEntityArgumentCaptorValue);
            assertEquals(expectedImageParam, imageParamArgumentCaptorValue);
        } else {
            verify(restaurantService, never()).updateRestaurantImage(expectedMultipartFile, expectedRestaurantEntity, expectedImageParam);
        }

    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void updateRestaurantImage_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/update-restaurant-image", restaurantEmail))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void updateRestaurantImage_shouldFailWhenIsNotLogin302() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/update-restaurant-image", restaurantEmail))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }


    //  updateRestaurantSchedule()
    @Test
    @WithMockUser(username = "business_user2", authorities = {"OWNER"})
    void updateRestaurantSchedule_shouldWorkCorrectlyWhenUserIsOwner() throws Exception {
        //  given
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity1();
        Restaurant expectedRestaurantModel = someRestaurantModel1();
        String restaurantEmail = "na_wypasie@restaurant.pl";

        when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(Optional.of(expectedRestaurantEntity));

        //  when
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/update-schedule", restaurantEmail)
                        .flashAttr("restaurant", expectedRestaurantModel)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/owner"));

        //  then
        ArgumentCaptor<RestaurantEntity> restaurantEntityArgumentCaptor = ArgumentCaptor.forClass(RestaurantEntity.class);
        ArgumentCaptor<Restaurant> restaurantArgumentCaptor = ArgumentCaptor.forClass(Restaurant.class);

        verify(restaurantService, times(1)).updateRestaurantSchedule(
                restaurantEntityArgumentCaptor.capture(),
                restaurantArgumentCaptor.capture()
        );

        RestaurantEntity restaurantEntityCaptorValue = restaurantEntityArgumentCaptor.getValue();
        Restaurant restaurantCaptorValue = restaurantArgumentCaptor.getValue();

        assertEquals(expectedRestaurantEntity, restaurantEntityCaptorValue);
        assertEquals(expectedRestaurantModel, restaurantCaptorValue);
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void updateRestaurantSchedule_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/update-schedule", restaurantEmail))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void updateRestaurantSchedule_shouldFailWhenIsNotLogin302() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/update-schedule", restaurantEmail))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }


    //  createFood()
    @ParameterizedTest
    @MethodSource("provideTestDataForFood")
    @WithMockUser(username = "business_user2", authorities = {"OWNER"})
    void createFood_shouldWorkCorrectlyDependingOnValidationWhenUserIsOwner(
            Food expectedFoodModel,
            boolean hasError,
            String errorMessage
    ) throws Exception {

        //  given
        RestaurantEntity expectedRestaurantEntity = someRestaurantEntity1();
        Restaurant expectedRestaurantModel = someRestaurantModel1();
        String restaurantEmail = "na_wypasie@restaurant.pl";

        when(restaurantJpaRepository.findByEmail(restaurantEmail)).thenReturn(Optional.of(expectedRestaurantEntity));
        when(restaurantEntityMapper.mapFromEntity(expectedRestaurantEntity)).thenReturn(expectedRestaurantModel);

        //  when
        BindingResult bindingResult = createBindingResult(hasError, errorMessage);

        //  then
        if (hasError) {
            mockMvc.perform(post("/owner/restaurant/{restaurantEmail}/create-food", restaurantEmail)
                            .flashAttr("food", expectedFoodModel)
                            .flashAttr(BindingResult.MODEL_KEY_PREFIX + "food", bindingResult)
                    )
                    .andExpect(view().name("owner_restaurant_details_edit"))
                    .andExpect(model().attributeExists("restaurant"))
                    .andExpect(model().attributeExists("defaultTab"))
                    .andExpect(content().string(containsString(errorMessage)));

            verify(foodMenuService, never()).assignFoodToFoodMenu(any(), any());
        } else {
            mockMvc.perform(post("/owner/restaurant/{restaurantEmail}/create-food", restaurantEmail)
                            .flashAttr("food", expectedFoodModel)
                            .flashAttr(BindingResult.MODEL_KEY_PREFIX + "food", bindingResult)
                    )
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/owner"));

            ArgumentCaptor<Food> foodArgumentCaptor = ArgumentCaptor.forClass(Food.class);
            ArgumentCaptor<RestaurantEntity> restaurantEntityArgumentCaptor = ArgumentCaptor.forClass(RestaurantEntity.class);

            verify(foodMenuService, times(1)).assignFoodToFoodMenu(
                    foodArgumentCaptor.capture(),
                    restaurantEntityArgumentCaptor.capture()
            );

            Food capturedFood = foodArgumentCaptor.getValue();
            RestaurantEntity capturedRestaurantEntity = restaurantEntityArgumentCaptor.getValue();

            assertEquals(expectedFoodModel, capturedFood);
            assertEquals(expectedRestaurantEntity, capturedRestaurantEntity);
        }
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void createFood_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/create-food", restaurantEmail))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void createFood_shouldFailWhenIsNotLogin302() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(patch("/owner/restaurant/{restaurantEmail}/create-food", restaurantEmail))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }

    private BindingResult createBindingResult(boolean hasError, String errorMessage) {
        BindingResult bindingResult = mock(BindingResult.class);
        if (hasError) {
            doAnswer(invocation -> {
                FieldError fieldError = new FieldError("food", "fileImageToUpload", errorMessage);
                bindingResult.addError(fieldError);
                return null;
            }).when(bindingResult).addError(any(FieldError.class));
            when(bindingResult.hasErrors()).thenReturn(true);
        } else {
            when(bindingResult.hasErrors()).thenReturn(false);
        }
        return bindingResult;
    }

    private static List<Object[]> provideTestDataForFood() {
        return Arrays.asList(new Object[][]{
                        {validFood(), false, ""},
                        {invalidFood(), true, "The image file is required."}
                }
        );
    }

    private static List<Object[]> provideTestDataForRestaurantImage() {
        return Arrays.asList(new Object[][]{
                {mockMultipartFile("valid-card-image.jpg", false), "CARD"},
                {mockMultipartFile("valid-header-image.jpg", false), "HEADER"},
                {mockMultipartFile("invalid-card-image.jpg", true), "CARD"},
                {mockMultipartFile("invalid-header-image.jpg", true), "HEADER"}
        });
    }

    private static Food validFood() {
        Food correctUpdatedFood = someFoodModel1();
        correctUpdatedFood.setFileImageToUpload(mockMultipartFile("valid-image.jpg", false));
        return correctUpdatedFood;
    }

    private static Food invalidFood() {
        Food incorrectlyUpdatedFood = someFoodModel2();
        incorrectlyUpdatedFood.setFileImageToUpload(mockMultipartFile("", true));
        return incorrectlyUpdatedFood;
    }

    private static MultipartFile mockMultipartFile(String filename, boolean isEmpty) {
        return new MockMultipartFile("restaurantImage", filename, "image/jpeg", isEmpty ? new byte[0] : "some-image-content".getBytes());
    }

    private static Stream<Arguments> provideTestData() {
        User userWithErrors = User.builder()
                .username("business_user")
                .email("user@business_user.com")
                .password("haslo")
                .role(UserRole.OWNER)
                .owner(RestaurantOwner.builder()
                        .name("Zbyszek")
                        .surname("Nazwisko")
                        .pesel("91014452879k")
                        .build())
                .build();

        User userWithoutErrors = User.builder()
                .username("business_user_correct")
                .email("user@business_user.com")
                .password("haslo")
                .role(UserRole.OWNER)
                .owner(RestaurantOwner.builder()
                        .name("Zbyszek")
                        .surname("Nazwisko")
                        .pesel("91014452879")
                        .build())
                .build();

        String correctEmail = "business@business_user2.com";
        List<String> errorMessages = createErrorMessagesListDependsOnFieldError(userWithErrors);


        return Stream.of(
                Arguments.of(userWithErrors, correctEmail, errorMessages, true),
                Arguments.of(userWithoutErrors, correctEmail, errorMessages, false)
        );
    }

    private static List<String> createErrorMessagesListDependsOnFieldError(User userForm) {
        List<String> errorMessages = new ArrayList<>();

        validateField(userForm.getUsername(), "Username is required.", errorMessages);
        validateField(userForm.getEmail(), "User email is required.", errorMessages);
        validateField(userForm.getOwner().getName(), "Name is required.", errorMessages);
        validateField(userForm.getOwner().getSurname(), "Surname is required.", errorMessages);
        validateField(userForm.getOwner().getPesel(), "Pesel is required.", errorMessages);

        if (userForm.getOwner().getPesel() != null) {
            validatePesel(userForm.getOwner().getPesel(), errorMessages);
        }

        if (userForm.getUsername() != null && userForm.getUsername().length() < 5) {
            errorMessages.add("Username must contain at least 5 characters.");
        }

        if (userForm.getPassword() != null && !userForm.getPassword().isEmpty() && userForm.getPassword().length() < 5) {
            errorMessages.add("Password must contain at least 5 characters.");
        }

        return errorMessages;
    }

    private static void validateField(String field, String errorMessage, List<String> errorMessages) {
        if (field == null || field.isBlank()) {
            errorMessages.add(errorMessage);
        }
    }

    private static void validatePesel(String pesel, List<String> errorMessages) {
        if (pesel.length() != 11) {
            errorMessages.add("Pesel number should have 11 numbers.");
        }
        if (!pesel.matches("^\\d+$")) {
            errorMessages.add("Pesel must contain only digits.");
        }
    }

}