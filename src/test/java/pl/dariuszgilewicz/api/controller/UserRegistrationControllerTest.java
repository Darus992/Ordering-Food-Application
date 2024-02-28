package pl.dariuszgilewicz.api.controller;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import static org.mockito.Mockito.verify;
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
        mockMvc.perform(MockMvcRequestBuilders.get("/user/register-customer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("customer_form"))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Customer Form")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Please enter your address:")))
                .andExpect(MockMvcResultMatchers.model().attribute("customerRequestForm", CoreMatchers.instanceOf(CustomerRequestForm.class)));
    }

    @Test
    @WithAnonymousUser
    void showBusinessRegistrationForm_workSuccessfully() throws Exception {
        //  given
        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.get("/user/register-business"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("business_form"))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Business Form")))
                .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Please enter restaurant details:")))
                .andExpect(MockMvcResultMatchers.model().attribute("businessRequestForm", CoreMatchers.instanceOf(BusinessRequestForm.class)));

    }

    @Test
    @WithAnonymousUser
    void createCustomerUserForm_workSuccessfully() throws Exception {
        //  given
        CustomerRequestForm customerRequestForm = someCustomerRequestForm();

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register-customer")
                        .flashAttr("customerRequestForm", customerRequestForm)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        verify(userService).createCustomerUser(customerRequestForm);

    }

    @Test
    @WithAnonymousUser
    void createBusinessUserForm_workSuccessfully() throws Exception {
        //  given
        BusinessRequestForm businessRequestForm = someBusinessRequestForm1();

        //  when
        //  then
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register-business")
                        .flashAttr("businessRequestForm", businessRequestForm)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        verify(userService).createBusinessUser(businessRequestForm);

    }
}