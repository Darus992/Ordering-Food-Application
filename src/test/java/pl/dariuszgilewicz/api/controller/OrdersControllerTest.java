package pl.dariuszgilewicz.api.controller;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.ui.Model;
import pl.dariuszgilewicz.business.OrderService;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.model.Cart;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.core.StringContains.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dariuszgilewicz.util.CartFixtures.someCartModel1;
import static pl.dariuszgilewicz.util.OrdersFixtures.someOrdersModel1;
import static pl.dariuszgilewicz.util.UsersFixtures.*;

@WebMvcTest(OrdersController.class)
@Import(SecurityConfiguration.class)
class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderRepository orderRepository;


    @ParameterizedTest
    @MethodSource("provideUserAuthorities")
    void showCartSummary_shouldWorkCorrectlyForAllAuthenticationType(Authentication authentication) throws Exception {
        //  given
        MockHttpSession session = new MockHttpSession();
        boolean isAuthenticated = addUserToSessionIfIsAuthenticated(authentication, session);

        String foodKeysJSON = "[{\"foodId\":1,\"name\":\"Pizza\",\"category\":\"Pizza\",\"description\":\"Delicious pizza\",\"price\":10.99}]";
        String foodValuesJSON = "[2]";
        BigDecimal totalPrice = BigDecimal.valueOf(21.98);
        String restaurantEmail = "restaurant@example.com";

        //  when
        //  then
        MvcResult result = mockMvc.perform(get("/order/cart-summary")
                        .session(session)
                        .flashAttr("isAuthenticated", isAuthenticated)
                        .param("foodKeys", foodKeysJSON)
                        .param("foodValues", foodValuesJSON)
                        .param("totalPrice", totalPrice.toString())
                        .param("restaurantEmail", restaurantEmail)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("cart_details"))
                .andExpect(model().attributeExists("isAuthenticated"))
                .andExpect(model().attribute("restaurantEmail", restaurantEmail))
                .andExpect(model().attribute("cart", CoreMatchers.instanceOf(Cart.class)))
                .andExpect(model().attribute("foodsId", List.of(1)))
                .andExpect(model().attribute("foodsValues", List.of(2)))
                .andExpect(content().string(containsString("Cart Details")))
                .andExpect(content().string(containsString("Cart Summary")))
                .andExpect(content().string(containsString("Total Price:")))
                .andExpect(content().string(containsString("zł")))
                .andExpect(content().string(containsString("Confirm Purchase")))
                .andExpect(content().string(containsString("Card Number:")))
                .andExpect(content().string(containsString("Name On Card:")))
                .andExpect(content().string(containsString("Expiry Date:")))
                .andExpect(content().string(containsString("Security Code:")))
                .andExpect(content().string(containsString("Pay")))
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        if (isAuthenticated) {
            assertTrue(contentAsString.contains("Logout"));
            assertTrue(contentAsString.contains("Your account"));

            assertFalse(contentAsString.contains("Log in"));
            assertFalse(contentAsString.contains("Register"));
            assertFalse(contentAsString.contains("Register Account Type"));
            assertFalse(contentAsString.contains("Which account type do you want to create?"));
            assertFalse(contentAsString.contains("Customer Account"));
            assertFalse(contentAsString.contains("Business Account"));
        } else {
            assertFalse(contentAsString.contains("Logout"));
            assertFalse(contentAsString.contains("Your account"));

            assertTrue(contentAsString.contains("Log in"));
            assertTrue(contentAsString.contains("Register"));
            assertTrue(contentAsString.contains("Register Account Type"));
            assertTrue(contentAsString.contains("Which account type do you want to create?"));
            assertTrue(contentAsString.contains("Customer Account"));
            assertTrue(contentAsString.contains("Business Account"));
        }
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void showOrderDetails_shouldWorkCorrectlyWhenUserIsCustomer200() throws Exception {
        //  given
        User expectedUser = someCustomerUserModel1();
        Orders expectedOrder = someOrdersModel1();
        Integer orderNumber = 12345;

        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(expectedUser));
        when(orderRepository.findOrderByOrderNumber(orderNumber)).thenReturn(expectedOrder);

        //  when
        //  then
        MvcResult mvcResult = mockMvc.perform(get("/order/{orderNumber}/details", orderNumber))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("order", expectedOrder))
                .andExpect(view().name("order_details"))
                .andReturn();

        verify(orderRepository, times(1)).findOrderByOrderNumber(orderNumber);

        findIfContentContainStringsForViewOrderDetails(mvcResult);
    }

    @Test
    @WithMockUser(username = "business_user", authorities = {"OWNER"})
    void showOrderDetails_shouldWorkCorrectlyWhenUserIsOwner200() throws Exception {
        //  given
        User expectedUser = someBusinessUserModel1();
        Orders expectedOrder = someOrdersModel1();
        Integer orderNumber = 12345;

        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(expectedUser));
        when(orderRepository.findOrderByOrderNumber(orderNumber)).thenReturn(expectedOrder);

        //  when
        //  then
        MvcResult mvcResult = mockMvc.perform(get("/order/{orderNumber}/details", orderNumber))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("order", expectedOrder))
                .andExpect(view().name("order_details"))
                .andReturn();

        verify(orderRepository, times(1)).findOrderByOrderNumber(orderNumber);

        findIfContentContainStringsForViewOrderDetails(mvcResult);

    }

    @Test
    @WithAnonymousUser
    void showOrderDetails_shouldFailWhenUserIsNoLogin302() throws Exception {
        //  given
        Integer orderNumber = 12345;
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(get("/order/{orderNumber}/details", orderNumber))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void createOrder_shouldWorkCorrectlyWhenUserIsCustomer() throws Exception {
        //  given
        User user = someCustomerUserModel1();
        Cart cart = someCartModel1();
        String jsonFoodId = "[1]";
        String jsonFoodValue = "[2]";
        BigDecimal totalPrice = BigDecimal.valueOf(21.98);
        String orderNotes = "The doorbell doesn't work, please give me a call.";
        String restaurantEmail = "restaurant@example.com";
        int orderNumber = 12345;

        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(user));
        when(orderService.createOrderAndReturnOrderNumber(
                cart.getFoodsIdArray(),
                cart.getFoodsValuesArray(),
                totalPrice,
                orderNotes,
                user,
                restaurantEmail
        )).thenReturn(orderNumber);

        //  when
        //  then
        mockMvc.perform(post("/order/create-order")
                        .param("foodsId", jsonFoodId)
                        .param("foodsValues", jsonFoodValue)
                        .param("totalPrice", totalPrice.toString())
                        .param("orderNotes", orderNotes)
                        .param("restaurantEmail", restaurantEmail)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/" + orderNumber + "/details"));


        verify(orderService, times(1)).createOrderAndReturnOrderNumber(
                cart.getFoodsIdArray(),
                cart.getFoodsValuesArray(),
                totalPrice,
                orderNotes,
                user,
                restaurantEmail
        );
    }

    @Test
    @WithMockUser(username = "business_user", authorities = {"OWNER"})
    void createOrder_shouldFailWhenUserIsOwner403() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(post("/order/create-order"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void createOrder_shouldFailWhenIsNoLogin302() throws Exception {
        //  given
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(post("/order/create-order"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private boolean addUserToSessionIfIsAuthenticated(Authentication authentication, MockHttpSession session) {
        Optional<? extends GrantedAuthority> optionalAuthority = authentication.getAuthorities().stream().findFirst();

        optionalAuthority.ifPresent(authority -> {
            String role = authority.getAuthority();
            User user;
            switch (role) {
                case "CUSTOMER" -> {
                    user = someCustomerUserModel1();
                    session.setAttribute("user", user);
                    when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(user));
                }
                case "OWNER" -> {
                    user = someBusinessUserModel1();
                    session.setAttribute("user", user);
                    when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(user));
                }
                default -> when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.empty());
            }
        });

        return !optionalAuthority.get().getAuthority().equals("ANONYMOUS");

    }

    private static Stream<Arguments> provideUserAuthorities() {
        return Stream.of(
                Arguments.of(createMockAuthentication("anonymous_user", "ANONYMOUS")),
                Arguments.of(createMockAuthentication("testowy_customer", "CUSTOMER")),
                Arguments.of(createMockAuthentication("testowy_owner", "OWNER"))
        );
    }

    private static Authentication createMockAuthentication(String username, String authority) {
        Collection<GrantedAuthority> authorities = Collections.singleton(() -> authority);
        if (authorities.stream().findFirst().get().getAuthority().equals("ANONYMOUS")) {
            return new AnonymousAuthenticationToken("anonymous", username, authorities);
        }

        return new UsernamePasswordAuthenticationToken(username, "haslo", authorities);
    }

    private static void findIfContentContainStringsForViewOrderDetails(MvcResult mvcResult) throws Exception {
        String contentAsString = mvcResult.getResponse().getContentAsString();

        assertTrue(contentAsString.contains("Order Details"));
        assertTrue(contentAsString.contains("Logout"));
        assertTrue(contentAsString.contains("Your account"));
        assertTrue(contentAsString.contains("Order Number:"));
        assertTrue(contentAsString.contains("Order Status:"));
        assertTrue(contentAsString.contains("Restaurant Address:"));
        assertTrue(contentAsString.contains("Restaurant Name:"));
        assertTrue(contentAsString.contains("Restaurant Phone:"));
        assertTrue(contentAsString.contains("Restaurant Email:"));
        assertTrue(contentAsString.contains("Received Date Time:"));
        assertTrue(contentAsString.contains("Delivery Address:"));
        assertTrue(contentAsString.contains("Customer Name:"));
        assertTrue(contentAsString.contains("Customer Surname:"));
        assertTrue(contentAsString.contains("Customer Phone:"));
        assertTrue(contentAsString.contains("Completed Date Time:"));
        assertTrue(contentAsString.contains("Total Price:"));
        assertTrue(contentAsString.contains("zł."));
        assertTrue(contentAsString.contains("Order Notes:"));
    }
}