package pl.dariuszgilewicz.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.ui.Model;
import pl.dariuszgilewicz.infrastructure.model.Customer;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dariuszgilewicz.util.OrdersFixtures.someOrdersModel1;
import static pl.dariuszgilewicz.util.UsersFixtures.*;

@WebMvcTest(CustomerController.class)
@Import(SecurityConfiguration.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @ParameterizedTest
    @MethodSource("provideUserData")
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void showCustomerPage_shouldWorkCorrectlyWhenUserIsCustomer200(User expectedUser, boolean hasOrders) throws Exception {
        //  given
        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(expectedUser));

        //  when
        //  then
        MvcResult result = mockMvc.perform(get("/customer")
                        .flashAttr("isAuthenticated", true)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("customer"))
                .andExpect(model().attributeExists("user", "userForm", "defaultTab", "isAuthenticated"))
                .andExpect(model().attribute("user", expectedUser))
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(containsString("Your account")))
                .andExpect(content().string(containsString("Orders History")))
                .andExpect(content().string(containsString("Account Details")))
                .andExpect(content().string(containsString("Username:")))
                .andExpect(content().string(containsString("Email:")))
                .andExpect(content().string(containsString("Password:")))
                .andExpect(content().string(containsString("Phone:")))
                .andExpect(content().string(containsString("+48")))
                .andExpect(content().string(containsString("Name:")))
                .andExpect(content().string(containsString("Surname:")))
                .andExpect(content().string(containsString("Delivery Address:")))
                .andExpect(content().string(containsString("Address:")))
                .andExpect(content().string(containsString("City:")))
                .andExpect(content().string(containsString("District:")))
                .andExpect(content().string(containsString("Postal Code:")))
                .andExpect(content().string(containsString("Save change")))
                .andExpect(content().string(containsString("Do you want to delete your account?")))
                .andExpect(content().string(containsString("Are you sure you want to delete your account?")))
                .andExpect(content().string(containsString("Order History")))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        if (hasOrders) {
            assertTrue(content.contains("Check Details"));
        } else {
            assertFalse(content.contains("Check Details"));
        }
    }

    @Test
    @WithMockUser(username = "business_user", authorities = {"OWNER"})
    void showCustomerPage_shouldFailWhenUserIsOwner403() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(get("/customer"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void showCustomerPage_shouldFailWhenIsNoLogin302() throws Exception {
        //  given
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(get("/customer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }

    @ParameterizedTest
    @MethodSource("provideUserDataForUpdateCustomer")
    @WithMockUser(username = "testowy_customer", authorities = {"CUSTOMER"})
    void updateCustomerDetailsPage_shouldWorkCorrectlyWhenUserIsCustomer200(
            User expectedUser,
            String emailParam,
            List<String> errorMessages,
            boolean hasErrors
    ) throws Exception {
        //  given
        User currentUser = someCustomerUserModel1();
        when(userService.getCurrentOptionalUser(any(Model.class))).thenReturn(Optional.of(currentUser));

        //  when
        //  then
        if (hasErrors) {
            ResultActions resultActions = mockMvc.perform(patch("/customer/update-profile")
                            .flashAttr("userForm", expectedUser)
                            .param("emailParam", emailParam)
                    )
                    .andExpect(view().name("customer"));

            for (String errorMessage : errorMessages) {
                resultActions.andExpect(content().string(containsString(errorMessage)));
            }
            verify(userService, never()).updateUserData(expectedUser, emailParam);
        } else {
            mockMvc.perform(patch("/customer/update-profile")
                            .flashAttr("userForm", expectedUser)
                            .param("emailParam", emailParam)
                    )
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/customer"));

            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<String> userEmailCaptor = ArgumentCaptor.forClass(String.class);
            verify(userService, times(1)).updateUserData(userArgumentCaptor.capture(), userEmailCaptor.capture());

            assertEquals(expectedUser, userArgumentCaptor.getValue());
            assertEquals(emailParam, userEmailCaptor.getValue());
        }
    }

    @Test
    @WithMockUser(username = "business_user", authorities = {"OWNER"})
    void updateCustomerDetailsPage_shouldFailWhenUserIsOwner403() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(get("/customer/update-profile"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void updateCustomerDetailsPage_shouldFailWhenIsNoLogin302() throws Exception {
        //  given
        String redirectPage = "http://localhost/login";

        //  when
        //  then
        mockMvc.perform(get("/customer/update-profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectPage));
    }

    private static Stream<Arguments> provideUserData() {
        User userWithOrders = someCustomerUserModel1();
        Orders order = someOrdersModel1();
        Customer customerWithOrders = userWithOrders.getCustomer();
        customerWithOrders.setCustomerOrders(List.of(order));

        User userWithoutOrders = someCustomerUserModel1();
        Customer customerWithoutOrders = userWithoutOrders.getCustomer();
        customerWithoutOrders.setCustomerOrders(Collections.emptyList());

        return Stream.of(
                Arguments.of(userWithOrders, true),
                Arguments.of(userWithoutOrders, false)
        );
    }

    private static Stream<Arguments> provideUserDataForUpdateCustomer() {
        User userWithErrors = someErrorCustomerUserModel1();
        User userWithErrors_2 = someErrorCustomerUserModel2();
        User userWithoutErrors = someCustomerUserModel1();

        String correctEmail = "testowy_customer@mail.com";
        List<String> errorMessages = createErrorMessagesListDependsOnFieldError(userWithErrors);
        List<String> errorMessages_2 = createErrorMessagesListDependsOnFieldError(userWithErrors_2);

        return Stream.of(
                Arguments.of(userWithErrors, correctEmail, errorMessages, true),
                Arguments.of(userWithErrors_2, correctEmail, errorMessages_2, true),
                Arguments.of(userWithoutErrors, correctEmail, errorMessages, false)
        );
    }

    private static List<String> createErrorMessagesListDependsOnFieldError(User userForm) {
        List<String> errorMessages = new ArrayList<>();

        validateField(userForm.getUsername(), "Username is required.", errorMessages);
        validateField(userForm.getEmail(), "User email is required.", errorMessages);
        validateField(userForm.getCustomer().getName(), "Name is required.", errorMessages);
        validateField(userForm.getCustomer().getSurname(), "Surname is required.", errorMessages);
        validateField(userForm.getCustomer().getPhone(), "Phone is required.", errorMessages);
        validateField(userForm.getCustomer().getAddress().getCity(), "City is required.", errorMessages);
        validateField(userForm.getCustomer().getAddress().getDistrict(), "District is required.", errorMessages);
        validateField(userForm.getCustomer().getAddress().getPostalCode(), "Postal code is required.", errorMessages);

        if (userForm.getCustomer().getPhone() != null) {
            validatePhone(userForm.getCustomer().getPhone(), errorMessages);
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

    private static void validatePhone(String phone, List<String> errorMessages) {
        if (phone.length() != 9) {
            errorMessages.add("The phone number size cannot be larger or smaller than 9.");
        }
        if (!phone.matches("^\\d+$")) {
            errorMessages.add("Phone number must contain only digits.");
        }
    }
}