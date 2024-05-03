package pl.dariuszgilewicz.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;
import pl.dariuszgilewicz.util.UsersFixtures;

import static org.mockito.Mockito.when;

@WebMvcTest(RestaurantOwnerController.class)
@Import(SecurityConfiguration.class)
class RestaurantOwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void showOwnerPage_shouldFailWhenUserIsCustomer403() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/owner"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void showOwnerPage_shouldFailWhenIsNoLoggIn302() throws Exception {
        //  given
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/owner"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectPage));
    }
}