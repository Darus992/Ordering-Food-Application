package pl.dariuszgilewicz.api.controller.rest;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.dariuszgilewicz.api.dto.RestaurantDTO;
import pl.dariuszgilewicz.api.dto.mapper.RestaurantMapper;
import pl.dariuszgilewicz.api.dto.mapper.RestaurantRequestFormMapper;
import pl.dariuszgilewicz.api.dto.request.RestaurantRequestFormDTO;
import pl.dariuszgilewicz.business.RestaurantService;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dariuszgilewicz.util.RestaurantFixtures.*;
import static pl.dariuszgilewicz.util.RestaurantRequestFormFixtures.someRestaurantRequestForm2;
import static pl.dariuszgilewicz.util.RestaurantRequestFormFixtures.someRestaurantRequestFormDTO2;
import static pl.dariuszgilewicz.util.UsersFixtures.someBusinessUserModel1;

@WebMvcTest(controllers = RestaurantRestController.class)
@Import(SecurityConfiguration.class)
class RestaurantRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private RestaurantMapper restaurantMapper;
    @MockBean
    private RestaurantRequestFormMapper restaurantRequestFormMapper;
    @MockBean
    private UserService userService;

    public final String BASE_URL = "http://localhost:8190/ordering-food-application";

    @Test
    void thatShouldReturnRestaurantDetailsListCorrectly() throws Exception {
        //  given
        Restaurant restaurant = someRestaurantModel2();
        Restaurant restaurant1 = someRestaurantModel5();
        List<RestaurantDTO> restaurantDTOS = someListOfRestaurantDTO2();
        List<Restaurant> restaurants = new ArrayList<>();
        String searchTerm = "Warszawa";

        restaurants.add(restaurant);
        restaurants.add(restaurant1);

        when(restaurantService.findRestaurantsBySearchTerm(searchTerm)).thenReturn(restaurants);
        when(restaurantMapper.mapToDTOList(restaurants, BASE_URL)).thenReturn(restaurantDTOS);

        //  when
        //  then
        mockMvc.perform(get("/api/restaurants")
                        .param("searchTerm", searchTerm))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

    }

    @Test
    void thatShouldGetRestaurantDetailsCorrectly() throws Exception {
        //  given
        Restaurant restaurant = someRestaurantModel2();
        RestaurantDTO restaurantDTO = someRestaurantDTO2();
        String restaurantEmail = "zapiecek@restaurant.pl";

        when(restaurantService.findRestaurantByEmail(restaurantEmail)).thenReturn(restaurant);
        when(restaurantMapper.mapToDTO(restaurant, BASE_URL)).thenReturn(restaurantDTO);

        //  when
        //  then
        mockMvc.perform(get("/api/restaurants/{restaurantEmail}", restaurantEmail))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurantImageCard", Matchers.is(restaurantDTO.getRestaurantImageCard())))
                .andExpect(jsonPath("$.restaurantImageHeader", Matchers.is(restaurantDTO.getRestaurantImageHeader())))
                .andExpect(jsonPath("$.restaurantName", Matchers.is(restaurantDTO.getRestaurantName())))
                .andExpect(jsonPath("$.restaurantPhone", Matchers.is(restaurantDTO.getRestaurantPhone())))
                .andExpect(jsonPath("$.restaurantEmail", Matchers.is(restaurantDTO.getRestaurantEmail())))
                .andExpect(jsonPath("$.restaurantCity", Matchers.is(restaurantDTO.getRestaurantCity())))
                .andExpect(jsonPath("$.restaurantDistrict", Matchers.is(restaurantDTO.getRestaurantDistrict())))
                .andExpect(jsonPath("$.restaurantPostalCode", Matchers.is(restaurantDTO.getRestaurantPostalCode())))
                .andExpect(jsonPath("$.restaurantAddressStreet", Matchers.is(restaurantDTO.getRestaurantAddressStreet())))
                .andExpect(jsonPath("$.openingHour", Matchers.is(restaurantDTO.getOpeningHour())))
                .andExpect(jsonPath("$.closeHour", Matchers.is(restaurantDTO.getCloseHour())))
                .andExpect(jsonPath("$.dayOfWeekFrom", Matchers.is(restaurantDTO.getDayOfWeekFrom())))
                .andExpect(jsonPath("$.dayOfWeekTill", Matchers.is(restaurantDTO.getDayOfWeekTill())))
                .andExpect(jsonPath("$.customerOrdersNumbers", Matchers.is(restaurantDTO.getCustomerOrdersNumbers())));
    }

    @Test
    void thatShouldCreateRestaurantCorrectly() throws Exception {
        //  given
        RestaurantRequestFormDTO requestFormDTO = someRestaurantRequestFormDTO2();
        RestaurantRequestForm requestForm = someRestaurantRequestForm2();
        User user = someBusinessUserModel1();

        when(restaurantRequestFormMapper.mapFromDTO(requestFormDTO)).thenReturn(requestForm);
        when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));

        //  when
        //  then
        mockMvc.perform(post("/api/restaurant/create"))
                .andExpect(content().string("Restaurant created successfully."))
                .andExpect(status().isCreated());
    }
}