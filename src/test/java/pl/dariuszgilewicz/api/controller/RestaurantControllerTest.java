package pl.dariuszgilewicz.api.controller;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import pl.dariuszgilewicz.business.RestaurantService;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someRestaurantModel1;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someRestaurantModel2;
import static pl.dariuszgilewicz.util.RestaurantRequestFormFixtures.someRestaurantRequestForm1;
import static pl.dariuszgilewicz.util.UsersFixtures.someBusinessUserModel1;
import static pl.dariuszgilewicz.util.UsersFixtures.someCustomerUserModel1;

@WebMvcTest(RestaurantController.class)
@Import(SecurityConfiguration.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private UserService userService;


    @Test
    @WithMockUser(username = "testowy_owner", authorities = {"OWNER"})
    void showRestaurantForm_shouldWorkCorrectlyWithAuthenticatedUser() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurant_form"))
                .andExpect(content().string(containsString("Create Restaurant:")))
                .andExpect(content().string(containsString("Restaurant Name:")))
                .andExpect(content().string(containsString("Restaurant Phone:")))
                .andExpect(content().string(containsString("Restaurant Email:")))
                .andExpect(content().string(containsString("Restaurant City:")))
                .andExpect(content().string(containsString("Restaurant Street:")))
                .andExpect(content().string(containsString("District:")))
                .andExpect(content().string(containsString("Postal Code:")))
                .andExpect(content().string(containsString("Opening Hour:")))
                .andExpect(content().string(containsString("Closing Hour:")))
                .andExpect(content().string(containsString("Starting Day:")))
                .andExpect(content().string(containsString("Finishing Day:")))
                .andExpect(model().attributeExists("restaurantRequestForm"))
                .andExpect(model().attribute("restaurantRequestForm", CoreMatchers.instanceOf(RestaurantRequestForm.class)));
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void showRestaurantForm_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/create"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void showRestaurantForm_shouldFailWhenIsNoLoggIn302() throws Exception {
        //  given
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }

    @ParameterizedTest
    @MethodSource("provideUserAuthorities")
    void showCurrentRestaurantDetails_shouldWorkCorrectly(Authentication authentication) throws Exception {
        //  given
        Restaurant restaurant = someRestaurantModel1();
        String restaurantEmail = restaurant.getRestaurantEmail();
        MockHttpSession session = new MockHttpSession();

        addUserToSessionIfIsAuthenticated(authentication, session);

        when(restaurantService.findRestaurantByEmail(restaurantEmail)).thenReturn(restaurant);

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/{restaurantEmail}", restaurantEmail)
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("restaurant_details"))
                .andExpect(content().string(containsString(restaurant.getRestaurantName())))
                .andExpect(model().attributeExists("restaurant"))
                .andExpect(model().attribute("restaurant", restaurant));
    }

    @ParameterizedTest
    @MethodSource("provideUserAuthorities")
    void findRestaurantsBySearchTerm_shouldWorkCorrectly(Authentication authentication) throws Exception {
        //  given
        MockHttpSession session = new MockHttpSession();
        Restaurant restaurant = someRestaurantModel1();
        Restaurant restaurant2 = someRestaurantModel2();
        List<Restaurant> restaurants = List.of(restaurant, restaurant2);
        String searchTerm = restaurant.getRestaurantAddress().getCity();
        int expectedSize = 2;

        addUserToSessionIfIsAuthenticated(authentication, session);
        session.setAttribute("restaurants", restaurants);

        when(restaurantService.findRestaurantsBySearchTerm(anyString())).thenReturn(restaurants);

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants")
                        .param("searchTerm", searchTerm)
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants_lists"))
                .andExpect(model().attributeExists("restaurants"))
                .andExpect(model().attribute("restaurants", CoreMatchers.instanceOf(List.class)))
                .andExpect(model().attribute("restaurants", Matchers.hasSize(expectedSize)));
    }

    @Test
    @WithMockUser(username = "business_user", authorities = {"OWNER"})
    void createRestaurantForm_shouldWorkCorrectlyWithAuthenticatedUser() throws Exception {
        //  given
        RestaurantRequestForm restaurantRequestForm = someRestaurantRequestForm1();
        User user = someBusinessUserModel1();
        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(user));

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/create")
                        .flashAttr("restaurantRequestForm", restaurantRequestForm)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/owner"));
    }

    @Test
    @WithMockUser(username = "testowy_Nickname", authorities = {"CUSTOMER"})
    void createRestaurantForm_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        User user = someCustomerUserModel1();
        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(user));

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/create"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void createRestaurantForm_shouldFailWhenIsNoLoggIn302() throws Exception {
        //  given
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }

    private void addUserToSessionIfIsAuthenticated(Authentication authentication, MockHttpSession session) {
        Optional.ofNullable(authentication)
                .ifPresent(auth -> {
                    boolean isAuthenticated = auth.getAuthorities().stream()
                            .findFirst()
                            .map(GrantedAuthority::getAuthority)
                            .map(role -> {
                                User user = role.equals("CUSTOMER") ? someCustomerUserModel1() : someBusinessUserModel1();
                                when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(user));
                                session.setAttribute("user", user);
                                return true;
                            })
                            .orElse(false);

                    session.setAttribute("isAuthenticated", isAuthenticated);
                });
    }

    private static Stream<Arguments> provideUserAuthorities() {
        return Stream.of(
                Arguments.of((Authentication) null),
                Arguments.of(createMockAuthentication("testowy_customer", "CUSTOMER")),
                Arguments.of(createMockAuthentication("testowy_owner", "OWNER"))
        );
    }

    private static Authentication createMockAuthentication(String username, String authority) {
        Collection<GrantedAuthority> authorities = Collections.singleton(() -> authority);
        return new UsernamePasswordAuthenticationToken(username, "haslo", authorities);
    }
}