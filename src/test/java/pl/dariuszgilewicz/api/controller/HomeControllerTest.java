package pl.dariuszgilewicz.api.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
                .andExpect(content().string(containsString("Find restaurants delivering right now, near you")))
                .andExpect(content().string(containsString("Log in")))
                .andExpect(content().string(containsString("Customer Register")))
                .andExpect(content().string(containsString("Business Register")))
                .andExpect(model().attribute("isAuthenticated", false));
    }


    @ParameterizedTest
    @MethodSource("provideUserAuthoritiesWithRoles")
    void testHomeForAuthenticatedUser(Authentication authentication, String userRole) throws Exception {
        //  given
        when(userService.checkIfIsAuthenticated(any(Model.class), any(Authentication.class))).thenReturn(true);

        //  when
        //  then
        mockMvc.perform(get("/")
                        .with(authentication(authentication))
                        .flashAttr("isAuthenticated", true)
                        .flashAttr("userRole", userRole)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("Find restaurants delivering right now, near you")))
                .andExpect(content().string(containsString("Menu")))
                .andExpect(content().string(containsString("Go to your account")))
                .andExpect(content().string(containsString("Logout")))
                .andExpect(model().attribute("isAuthenticated", true));
    }

    private static Stream<Arguments> provideUserAuthoritiesWithRoles() {
        return Stream.of(
                Arguments.of(createMockAuthentication("testowy_customer", "CUSTOMER"), "CUSTOMER"),
                Arguments.of(createMockAuthentication("testowy_owner", "OWNER"), "OWNER")
        );
    }

    private static Authentication createMockAuthentication(String username, String authority) {
        Collection<GrantedAuthority> authorities = Collections.singleton(() -> authority);
        return new UsernamePasswordAuthenticationToken(username, "haslo", authorities);
    }
}