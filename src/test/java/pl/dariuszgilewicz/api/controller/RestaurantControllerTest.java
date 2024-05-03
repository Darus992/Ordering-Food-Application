package pl.dariuszgilewicz.api.controller;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import pl.dariuszgilewicz.business.FoodMenuService;
import pl.dariuszgilewicz.business.RestaurantService;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.FoodFixtures.someFood1;
import static pl.dariuszgilewicz.util.FoodMenuFixtures.someFoodMenu2;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someListOfMappedRestaurants1;
import static pl.dariuszgilewicz.util.RestaurantFixtures.someRestaurantModel1;
import static pl.dariuszgilewicz.util.RestaurantRequestFormFixtures.someRestaurantRequestForm1;

@WebMvcTest(RestaurantController.class)
@Import(SecurityConfiguration.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodMenuService foodMenuService;

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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("restaurant_form"))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Create Restaurant:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Restaurant Name:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Restaurant Phone:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Restaurant Email:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Restaurant City:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Restaurant Street:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("District:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Postal Code:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Opening Hour:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Closing Hour:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Starting Day:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Finishing Day:")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("restaurantRequestForm"))
                .andExpect(MockMvcResultMatchers.model().attribute("restaurantRequestForm", CoreMatchers.instanceOf(RestaurantRequestForm.class)));
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void showRestaurantForm_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/create"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void showRestaurantForm_shouldFailWhenIsNoLoggIn302() throws Exception {
        //  given
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/create"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectPage));
    }

    @ParameterizedTest
    @MethodSource("provideUserAuthorities")
    void showCurrentRestaurantDetails_shouldWorkCorrectly(Authentication authentication) throws Exception {
        //  given
        Restaurant restaurant = someRestaurantModel1();
        String restaurantEmail = restaurant.getRestaurantEmail();
        boolean isAuthenticated = authentication != null;
        when(restaurantService.findRestaurantByEmail(restaurantEmail)).thenReturn(restaurant);
        when(userService.checkIfIsAuthenticated(any(Model.class), any(Authentication.class))).thenReturn(isAuthenticated);

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/{restaurantEmail}", restaurantEmail)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(authentication))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("restaurant_details"))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Phone")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("restaurantEmail"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("restaurant"))
                .andExpect(MockMvcResultMatchers.model().attribute("restaurantEmail", restaurantEmail))
                .andExpect(MockMvcResultMatchers.model().attribute("restaurant", restaurant));
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void findRestaurantsBySearchTerm_shouldWorkCorrectlyWithAuthenticatedCustomer() throws Exception {
        //  given
        Restaurant restaurant = someRestaurantModel1();
        String searchTerm = restaurant.getRestaurantAddress().getCity();
        when(restaurantService.findRestaurantsBySearchTerm(anyString())).thenReturn(List.of(restaurant));
        when(userService.checkIfIsAuthenticated(any(Model.class), any(Authentication.class))).thenReturn(true);
        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants")
                        .param("searchTerm", searchTerm)
                        .requestAttr("isAuthenticated", true)
                        .flashAttr("userRole", "CUSTOMER")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("restaurants_lists"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("restaurants"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userRole"))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Logout")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Go to your account")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Available Restaurants:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Go To Restaurant")))
                .andExpect(MockMvcResultMatchers.model().attribute("restaurants", CoreMatchers.instanceOf(List.class)));
    }

    @Test
    @WithMockUser(username = "testowy_owner", authorities = {"OWNER"})
    void findRestaurantsBySearchTerm_shouldWorkCorrectlyWithAuthenticatedOwner() throws Exception {
        //  given
        Restaurant restaurant = someRestaurantModel1();
        String searchTerm = restaurant.getRestaurantAddress().getCity();
        when(restaurantService.findRestaurantsBySearchTerm(anyString())).thenReturn(List.of(restaurant));
        when(userService.checkIfIsAuthenticated(any(Model.class), any(Authentication.class))).thenReturn(true);
        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants")
                        .param("searchTerm", searchTerm)
                        .requestAttr("isAuthenticated", true)
                        .flashAttr("userRole", "OWNER")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("restaurants_lists"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("restaurants"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userRole"))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Logout")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Go to your account")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Available Restaurants:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Go To Restaurant")))
                .andExpect(MockMvcResultMatchers.model().attribute("restaurants", CoreMatchers.instanceOf(List.class)));
    }

    @Test
    @WithAnonymousUser
    void findRestaurantsBySearchTerm_shouldWorkCorrectlyWithoutLoggIn() throws Exception {
        //  given
        Restaurant restaurant = someRestaurantModel1();
        String searchTerm = restaurant.getRestaurantAddress().getCity();
        when(restaurantService.findRestaurantsBySearchTerm(anyString())).thenReturn(List.of(restaurant));
        when(userService.checkIfIsAuthenticated(any(Model.class), any(Authentication.class))).thenReturn(false);
        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants")
                        .param("searchTerm", searchTerm)
                        .requestAttr("isAuthenticated", false)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("restaurants_lists"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("restaurants"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("userRole"))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Log in")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Customer Register")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Business Register")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Available Restaurants:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Go To Restaurant")))
                .andExpect(MockMvcResultMatchers.model().attribute("restaurants", CoreMatchers.instanceOf(List.class)));
    }

    @ParameterizedTest
    @MethodSource("provideUserAuthorities")
    void showRestaurantsWithPickedCategory_shouldWorkCorrectly(Authentication authentication) throws Exception {
        //  given
        List<Restaurant> restaurants = someListOfMappedRestaurants1();
        boolean isAuthenticated = authentication != null;
        String foodCategory = "Burgers";
        when(restaurantService.findAllRestaurantsWithSelectedCategory(foodCategory)).thenReturn(restaurants);
        when(userService.checkIfIsAuthenticated(any(Model.class), any(Authentication.class))).thenReturn(isAuthenticated);

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/category")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(authentication))
                        .param("foodCategory", foodCategory)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("restaurants_lists"))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Pizza")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Burgers")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Home Cooked Dinners")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Sushi")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Fish & Seafood")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("selectedCategory"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("restaurants"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                .andExpect(MockMvcResultMatchers.model().attribute("categories", CoreMatchers.instanceOf(List.class)))
                .andExpect(MockMvcResultMatchers.model().attribute("restaurants", restaurants));
    }

    @Test
    @WithMockUser(username = "testowy_owner", authorities = {"OWNER"})
    void showFoodMenuForm_shouldWorkCorrectlyWithAuthenticatedUser() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/create-food-menu/{restaurantEmail}", restaurantEmail))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("food_menu_form"))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Food Menu Form")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Please enter data:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Food Menu Name:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Food Category:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Food Name:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Description:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Price:")))
                .andExpect(MockMvcResultMatchers.model().attribute("foodMenuForm", CoreMatchers.instanceOf(FoodMenu.class)))
                .andExpect(MockMvcResultMatchers.model().attribute("restaurantEmail", restaurantEmail));

    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void showFoodMenuForm_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/create-food-menu/{restaurantEmail}", restaurantEmail))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void showFoodMenuForm_shouldFailWhenIsNoLoggIn302() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/create-food-menu/{restaurantEmail}", restaurantEmail))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectPage));
    }

    @Test
    @WithMockUser(username = "testowy_owner", authorities = {"OWNER"})
    void showFoodForm_shouldWorkCorrectlyWithAuthenticatedUser() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/create-food/{restaurantEmail}", restaurantEmail))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("food_form"))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Food Form")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Please enter data:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Food Category:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Food Name:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Description:")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Price:")))
                .andExpect(MockMvcResultMatchers.model().attribute("foodForm", CoreMatchers.instanceOf(Food.class)))
                .andExpect(MockMvcResultMatchers.model().attribute("restaurantEmail", restaurantEmail));
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void showFoodForm_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/create-food/{restaurantEmail}", restaurantEmail))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void showFoodForm_shouldFailWhenIsNoLoggIn302() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/create-food/{restaurantEmail}", restaurantEmail))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectPage));
    }

    @Test
    @WithMockUser(username = "testowy_owner", authorities = {"OWNER"})
    void createRestaurantForm_shouldWorkCorrectlyWithAuthenticatedUser() throws Exception {
        //  given
        RestaurantRequestForm restaurantRequestForm = someRestaurantRequestForm1();
        String username = "testowy_owner";
        when(userService.getCurrentUserName()).thenReturn(username);

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/create")
                        .flashAttr("restaurantRequestForm", restaurantRequestForm)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/owner"));
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void createRestaurantForm_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String username = "testowy_customer";
        when(userService.getCurrentUserName()).thenReturn(username);

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/create"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void createRestaurantForm_shouldFailWhenIsNoLoggIn302() throws Exception {
        //  given
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/create"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectPage));
    }

    @Test
    @WithMockUser(username = "testowy_owner", authorities = {"OWNER"})
    void createFoodMenuForm_shouldWorkCorrectlyWithAuthenticatedUser() throws Exception {
        //  given
        FoodMenu foodMenu = someFoodMenu2();
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/create-food-menu/{restaurantEmail}", restaurantEmail)
                        .param("restaurantEmail", restaurantEmail)
                        .flashAttr("foodMenuForm", foodMenu)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/owner"));
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void createFoodMenuForm_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/create-food-menu/{restaurantEmail}", restaurantEmail)
                        .param("restaurantEmail", restaurantEmail)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void createFoodMenuForm_shouldFailWhenIsNoLoggIn302() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/create-food-menu/{restaurantEmail}", restaurantEmail)
                        .param("restaurantEmail", restaurantEmail)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectPage));
    }

    @Test
    @WithMockUser(username = "testowy_owner", authorities = {"OWNER"})
    void createFoodForm_shouldWorkCorrectlyWithAuthenticatedUser() throws Exception {
        //  given
        Food food = someFood1();
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/create-food/{restaurantEmail}", restaurantEmail)
                        .param("restaurantEmail", restaurantEmail)
                        .flashAttr("foodForm", food)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/owner"));
    }

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void createFoodForm_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/create-food/{restaurantEmail}", restaurantEmail)
                        .param("restaurantEmail", restaurantEmail)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void createFoodForm_shouldFailWhenIsNoLoggIn302() throws Exception {
        //  given
        String restaurantEmail = "na_wypasie@restaurant.pl";
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant/create-food/{restaurantEmail}", restaurantEmail)
                        .param("restaurantEmail", restaurantEmail)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectPage));
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