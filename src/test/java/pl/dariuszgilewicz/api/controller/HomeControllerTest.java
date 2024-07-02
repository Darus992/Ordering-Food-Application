package pl.dariuszgilewicz.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dariuszgilewicz.util.UsersFixtures.*;


@WebMvcTest(HomeController.class)
@Import(SecurityConfiguration.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;


    @Test
    @WithAnonymousUser
    void testHomeForUnauthenticatedUser() throws Exception {
        //  given
        when(userService.checkIfIsAuthenticated(any(Model.class), any(Authentication.class))).thenReturn(false);

        //  when
        //  then
        mockMvc.perform(get("/")
                        .flashAttr("isAuthenticated", false)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("Register Account Type")))
                .andExpect(content().string(containsString("Which account type do you want to create?")))
                .andExpect(content().string(containsString("Log in")))
                .andExpect(content().string(containsString("Search")))
                .andExpect(model().attribute("isAuthenticated", false));
    }

    @ParameterizedTest
    @MethodSource("userRolesProvider")
    void testHomeForAuthenticatedUser(User user, String expectedContent) throws Exception {
        //  given
        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(user));

        //  when
        //  then
        mockMvc.perform(get("/")
                        .flashAttr("isAuthenticated", true)
                        .flashAttr("user", user)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString(expectedContent)))
                .andExpect(content().string(containsString("Logout")))
                .andExpect(model().attribute("isAuthenticated", true));
    }

    static Collection<Object[]> userRolesProvider() {
        return Arrays.asList(new Object[][]{
                {someBusinessUserModel1(), "Your account"},
                {someBusinessUserModel2(), "Your account"},
                {someCustomerUserModel1(), "Your account"}
        });
    }
}