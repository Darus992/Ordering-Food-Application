package pl.dariuszgilewicz.api.controller;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.someBusinessRequestForm1;
import static pl.dariuszgilewicz.util.CustomerRequestFormFixtures.someCustomerRequestForm;

@WebMvcTest(UserRegistrationController.class)
@Import(SecurityConfiguration.class)
class UserRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    @WithAnonymousUser
    void showCustomerRegistrationForm_workSuccessfully() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(get("/user/register-customer"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer_form"))
                .andExpect(content().string(containsString("Customer Form")))
                .andExpect(content().string(containsString("Go to your account")))
                .andExpect(content().string(containsString("Create Account:")))
                .andExpect(content().string(containsString("Username:")))
                .andExpect(content().string(containsString("Email:")))
                .andExpect(content().string(containsString("Password:")))
                .andExpect(content().string(containsString("Name:")))
                .andExpect(content().string(containsString("Surname:")))
                .andExpect(content().string(containsString("Phone:")))
                .andExpect(content().string(containsString("Please enter your address:")))
                .andExpect(content().string(containsString("City:")))
                .andExpect(content().string(containsString("Street:")))
                .andExpect(content().string(containsString("District:")))
                .andExpect(content().string(containsString("Postal Code:")))
                .andExpect(content().string(containsString("Already have an account ?")))
                .andExpect(content().string(containsString("Click here")))
                .andExpect(model().attribute("customerRequestForm", CoreMatchers.instanceOf(CustomerRequestForm.class)));
    }

    @Test
    @WithAnonymousUser
    void showBusinessRegistrationForm_workSuccessfully() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(get("/user/register-business")
                        .flashAttr("isAuthenticated", false)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("business_form"))
                .andExpect(content().string(containsString("Business Form")))
                .andExpect(content().string(containsString("Log in")))
                .andExpect(content().string(containsString("Register")))
                .andExpect(content().string(containsString("Create Account:")))
                .andExpect(content().string(containsString("Username:")))
                .andExpect(content().string(containsString("Email:")))
                .andExpect(content().string(containsString("Password:")))
                .andExpect(content().string(containsString("Name:")))
                .andExpect(content().string(containsString("Surname:")))
                .andExpect(content().string(containsString("Pesel:")))
                .andExpect(content().string(containsString("Please enter restaurant details:")))
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
                .andExpect(content().string(containsString("Card Image:")))
                .andExpect(content().string(containsString("Header Image:")))
                .andExpect(content().string(containsString("Create Account")))
                .andExpect(content().string(containsString("Cancel")))
                .andExpect(content().string(containsString("Already have an account ?")))
                .andExpect(content().string(containsString("Click here")))
                .andExpect(model().attribute("businessRequestForm", CoreMatchers.instanceOf(BusinessRequestForm.class)));

    }

    @Test
    @WithAnonymousUser
    void createCustomerUserForm_workSuccessfully() throws Exception {
        //  given
        CustomerRequestForm expectedCustomerRequestForm = someCustomerRequestForm();

        //  when
        //  then
        mockMvc.perform(post("/user/register-customer")
                        .flashAttr("customerRequestForm", expectedCustomerRequestForm)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        ArgumentCaptor<CustomerRequestForm> captor = ArgumentCaptor.forClass(CustomerRequestForm.class);
        verify(userService).createCustomerUser(captor.capture());

        CustomerRequestForm captorValue = captor.getValue();
        assertEquals(expectedCustomerRequestForm, captorValue);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    @WithAnonymousUser
    void createBusinessUserForm_workSuccessfully(
            BusinessRequestForm expectedBusinessRequestForm,
            boolean hasError,
            String errorMessage
    ) throws Exception {

        //  given
        BindingResult bindingResult = mock(BindingResult.class);

        //  when
        //  then
        if (hasError) {
            mockMvc.perform(multipart("/user/register-business")
                            .flashAttr("businessRequestForm", expectedBusinessRequestForm)
                            .flashAttr(BindingResult.MODEL_KEY_PREFIX + "businessRequestForm", bindingResult)
                    )
                    .andExpect(view().name("business_form"))
                    .andExpect(model().hasErrors())
                    .andExpect(content().string(containsString(errorMessage)));

            verify(userService, never()).createBusinessUser(expectedBusinessRequestForm);
        } else {
            mockMvc.perform(multipart("/user/register-business")
                            .flashAttr("businessRequestForm", expectedBusinessRequestForm)
                            .flashAttr(BindingResult.MODEL_KEY_PREFIX + "businessRequestForm", bindingResult)
                    )
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));

            ArgumentCaptor<BusinessRequestForm> captor = ArgumentCaptor.forClass(BusinessRequestForm.class);
            verify(userService, times(1)).createBusinessUser(captor.capture());

            BusinessRequestForm captorValue = captor.getValue();
            assertEquals(expectedBusinessRequestForm, captorValue);
        }

    }

    private static List<Object[]> provideTestData() {
        return Arrays.asList(new Object[][]{
                {validBusinessRequestForm(), false, ""},
                {invalidBusinessRequestForm(), true, "The image file is required."}
        });
    }

    private static BusinessRequestForm validBusinessRequestForm() {
        BusinessRequestForm valid = someBusinessRequestForm1();
        valid.setRestaurantImageCard(mockMultipartFile("valid-card-image-business-request.jpg", false));
        valid.setRestaurantImageHeader(mockMultipartFile("valid-header-image-business-request.jpg", false));
        return valid;
    }

    private static BusinessRequestForm invalidBusinessRequestForm() {
        BusinessRequestForm invalid = someBusinessRequestForm1();
        invalid.setRestaurantImageCard(mockMultipartFile("", true));
        invalid.setRestaurantImageHeader(mockMultipartFile("", true));
        return invalid;
    }

    private static MultipartFile mockMultipartFile(String filename, boolean isEmpty) {
        return new MockMultipartFile("businessRequestForm", filename, "image/jpeg", isEmpty ? new byte[0] : "some-image-content".getBytes());
    }
}