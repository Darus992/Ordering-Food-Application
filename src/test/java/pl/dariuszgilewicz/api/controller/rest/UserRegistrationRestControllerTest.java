package pl.dariuszgilewicz.api.controller.rest;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import pl.dariuszgilewicz.api.dto.BusinessUserDTO;
import pl.dariuszgilewicz.api.dto.CustomerUserDTO;
import pl.dariuszgilewicz.api.dto.mapper.BusinessUserMapper;
import pl.dariuszgilewicz.api.dto.mapper.CustomerUserMapper;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.someBusinessRequestForm3;
import static pl.dariuszgilewicz.util.BusinessUserDTOFixtures.someBusinessUserDTO1;
import static pl.dariuszgilewicz.util.CustomerRequestFormFixtures.someCustomerRequestForm;
import static pl.dariuszgilewicz.util.CustomerUserDTOFixtures.someCustomerUserDTO1;

@WebMvcTest(controllers = UserRegistrationRestController.class)
@Import(SecurityConfiguration.class)
class UserRegistrationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private CustomerUserMapper customerUserMapper;
    @MockBean
    private BusinessUserMapper businessUserMapper;


    @Test
    void thatShouldCreateCustomerUserCorrectly() throws Exception {
        //  given
        CustomerUserDTO customerUserDTO = someCustomerUserDTO1();
        CustomerRequestForm customerRequestForm = someCustomerRequestForm();
        customerUserDTO.setPassword("haslo");

        when(customerUserMapper.mapFromDTOToCustomerRequestForm(customerUserDTO)).thenReturn(customerRequestForm);

        //  when
        //  then
        mockMvc.perform(post("/api/register-customer")
                        .header("customerUserDTO", customerUserDTO)
                )
                .andExpect(content().string("Customer user register successfully."))
                .andExpect(status().isOk());

        ArgumentCaptor<CustomerRequestForm> requestFormArgumentCaptor = ArgumentCaptor.forClass(CustomerRequestForm.class);

        verify(userService, times(1)).createCustomerUser(requestFormArgumentCaptor.capture());
    }

    @Test
    void thatShouldCreateBusinessUserCorrectly() throws Exception {
        //  given
        BusinessUserDTO businessUserDTO = someBusinessUserDTO1();
        BusinessRequestForm requestForm = someBusinessRequestForm3();

        when(businessUserMapper.mapFromDTOToBusinessRequestForm(businessUserDTO)).thenReturn(requestForm);

        //  when
        //  then
        mockMvc.perform(multipart("/api/register-business")
                        .file("restaurantImageCard", businessUserDTO.getRestaurantImageCard().getBytes())
                        .file("restaurantImageHeader", businessUserDTO.getRestaurantImageHeader().getBytes())
                        .param("username", businessUserDTO.getUsername())
                        .param("password", businessUserDTO.getPassword())
                        .param("email", businessUserDTO.getEmail())
                        .param("ownerName", businessUserDTO.getOwnerName())
                        .param("ownerSurname", businessUserDTO.getOwnerSurname())
                        .param("ownerPesel", businessUserDTO.getOwnerPesel())
                        .param("restaurantName", businessUserDTO.getRestaurantName())
                        .param("restaurantPhone", businessUserDTO.getRestaurantPhone())
                        .param("restaurantEmail", businessUserDTO.getRestaurantEmail())
                        .param("restaurantAddressCity", businessUserDTO.getRestaurantAddressCity())
                        .param("restaurantAddressDistrict", businessUserDTO.getRestaurantAddressDistrict())
                        .param("restaurantAddressPostalCode", businessUserDTO.getRestaurantAddressPostalCode())
                        .param("restaurantAddressStreet", businessUserDTO.getRestaurantAddressStreet())
                        .param("openingHour", businessUserDTO.getOpeningHour())
                        .param("closeHour", businessUserDTO.getCloseHour())
                        .param("dayOfWeekFrom", businessUserDTO.getDayOfWeekFrom().name())
                        .param("dayOfWeekTill", businessUserDTO.getDayOfWeekTill().name())
                )
                .andExpect(content().string("Business user register successfully."))
                .andExpect(status().isOk());

        ArgumentCaptor<BusinessRequestForm> requestFormArgumentCaptor = ArgumentCaptor.forClass(BusinessRequestForm.class);

        verify(userService, times(1)).createBusinessUser(requestFormArgumentCaptor.capture());
    }
}