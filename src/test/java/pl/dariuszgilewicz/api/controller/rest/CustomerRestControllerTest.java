package pl.dariuszgilewicz.api.controller.rest;

import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.dariuszgilewicz.api.dto.CustomerUserDTO;
import pl.dariuszgilewicz.api.dto.OrderDTO;
import pl.dariuszgilewicz.api.dto.mapper.CustomerUserMapper;
import pl.dariuszgilewicz.api.dto.mapper.OrderMapper;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.security.SecurityConfiguration;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dariuszgilewicz.util.CustomerUserDTOFixtures.someCustomerUserDTO1;
import static pl.dariuszgilewicz.util.OrdersFixtures.*;
import static pl.dariuszgilewicz.util.UsersFixtures.someBusinessUserModel1;
import static pl.dariuszgilewicz.util.UsersFixtures.someCustomerUserModel1;

@WebMvcTest(controllers = CustomerRestController.class)
@Import(SecurityConfiguration.class)
class CustomerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private CustomerUserMapper customerUserMapper;
    @MockBean
    private OrderMapper orderMapper;
    @MockBean
    private OrderRepository orderRepository;

    private static final String BASE_PATH = "/api/customer";


    @ParameterizedTest
    @MethodSource("provideUser")
    void thatCustomerDetailsCanBeRetrievedCorrectly(User user, boolean isCustomer) throws Exception {
        //  given
        //  when
        //  then
        if (isCustomer && user != null) {
            CustomerUserDTO customerUserDTO = someCustomerUserDTO1();

            when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));
            when(customerUserMapper.mapToDTO(user)).thenReturn(customerUserDTO);


            mockMvc.perform(get(BASE_PATH))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username", Matchers.is(customerUserDTO.getUsername())))
                    .andExpect(jsonPath("$.email", Matchers.is(customerUserDTO.getEmail())))
                    .andExpect(jsonPath("$.password", Matchers.is(customerUserDTO.getPassword())))
                    .andExpect(jsonPath("$.customerName", Matchers.is(customerUserDTO.getCustomerName())))
                    .andExpect(jsonPath("$.customerSurname", Matchers.is(customerUserDTO.getCustomerSurname())))
                    .andExpect(jsonPath("$.customerPhone", Matchers.is(customerUserDTO.getCustomerPhone())))
                    .andExpect(jsonPath("$.addressCity", Matchers.is(customerUserDTO.getAddressCity())))
                    .andExpect(jsonPath("$.addressDistrict", Matchers.is(customerUserDTO.getAddressDistrict())))
                    .andExpect(jsonPath("$.addressPostalCode", Matchers.is(customerUserDTO.getAddressPostalCode())))
                    .andExpect(jsonPath("$.addressStreet", Matchers.is(customerUserDTO.getAddressStreet())));
        } else {
            mockMvc.perform(get(BASE_PATH))
                    .andExpect(status().isUnauthorized());
        }
    }

    @ParameterizedTest
    @MethodSource("provideUser")
    void thatCustomerOrderHistoryCanBeRetrievedCorrectly(User user, boolean isCustomer) throws Exception {
        //  given
        //  when
        //  then
        if (isCustomer && user != null) {
            List<Orders> ordersList = someOrdersModelList2();
            List<OrderDTO> orderDTOS = someOrdersDTOList2();

            user.getCustomer().setCustomerOrders(ordersList);

            when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));
            when(orderMapper.mapToDTOList(any())).thenReturn(orderDTOS);

            mockMvc.perform(get(BASE_PATH + "/orders-history"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());

            verify(orderMapper, times(1)).mapToDTOList(any());
        } else {
            mockMvc.perform(get(BASE_PATH + "/orders-history"))
                    .andExpect(status().isUnauthorized());

            verify(orderMapper, times(0)).mapToDTOList(any());
        }
        
        verify(userService, times(1)).getCurrentOptionalUser();
    }

    @ParameterizedTest
    @MethodSource("provideUser")
    void thatOrderDetailsCanBeRetrievedCorrectly(User user, boolean isCustomer) throws Exception {
        //  given
        String orderNumber = "12345";

        //  when
        //  then
        if (isCustomer && user != null) {
            Orders order = someOrdersModel1();
            OrderDTO orderDTO = someOrderDTO1();

            when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));
            when(orderRepository.findOrderByOrderNumber(Integer.parseInt(orderNumber))).thenReturn(order);
            when(orderMapper.mapToDTO(order)).thenReturn(orderDTO);

            mockMvc.perform(get(BASE_PATH + "/order/{orderNumber}/details", orderNumber))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orderNumber", Matchers.is(orderDTO.getOrderNumber())))
                    .andExpect(jsonPath("$.orderStatus", Matchers.is(orderDTO.getOrderStatus())))
                    .andExpect(jsonPath("$.orderNotes", Matchers.is(orderDTO.getOrderNotes())))
                    .andExpect(jsonPath("$.orderReceivedDateTime", Matchers.is(orderDTO.getOrderReceivedDateTime())))
                    .andExpect(jsonPath("$.orderCompletedDateTime", Matchers.is(orderDTO.getOrderCompletedDateTime())))
                    .andExpect(jsonPath("$.orderFoodRequests").isArray())
                    .andExpect(jsonPath("$.orderTotalPrice", Matchers.is(orderDTO.getOrderTotalPrice())))
                    .andExpect(jsonPath("$.customerName", Matchers.is(orderDTO.getCustomerName())))
                    .andExpect(jsonPath("$.customerSurname", Matchers.is(orderDTO.getCustomerSurname())))
                    .andExpect(jsonPath("$.customerPhone", Matchers.is(orderDTO.getCustomerPhone())))
                    .andExpect(jsonPath("$.customerAddressCity", Matchers.is(orderDTO.getCustomerAddressCity())))
                    .andExpect(jsonPath("$.customerAddressDistrict", Matchers.is(orderDTO.getCustomerAddressDistrict())))
                    .andExpect(jsonPath("$.customerAddressPostalCode", Matchers.is(orderDTO.getCustomerAddressPostalCode())))
                    .andExpect(jsonPath("$.customerAddressStreet", Matchers.is(orderDTO.getCustomerAddressStreet())));

            verify(orderRepository, times(1)).findOrderByOrderNumber(any());
            verify(orderMapper, times(1)).mapToDTO(any());
        } else {
            mockMvc.perform(get(BASE_PATH + "/order/{orderNumber}/details", orderNumber))
                    .andExpect(status().isUnauthorized());

            verify(orderRepository, times(0)).findOrderByOrderNumber(any());
            verify(orderMapper, times(0)).mapToDTO(any());
        }
    }

    @ParameterizedTest
    @MethodSource("provideUser")
    void thatUpdateCustomerDetailsShouldWorkCorrectly(User user, boolean isCustomer) throws Exception {
        //  given
        CustomerUserDTO customerUserDTO = someCustomerUserDTO1();
        ObjectMapper objectMapper = new ObjectMapper();

        customerUserDTO.setCustomerName("Michał");
        customerUserDTO.setCustomerSurname("Nowacki");

        //  when
        //  then
        if (isCustomer && user != null) {

            User updatedUserData = new User(
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole(),
                    null,
                    user.getCustomer()
            );

            updatedUserData.getCustomer().setName(customerUserDTO.getCustomerName());
            updatedUserData.getCustomer().setSurname(customerUserDTO.getCustomerSurname());

            when(userService.getCurrentOptionalUser()).thenReturn(Optional.of(user));
            when(customerUserMapper.mapFromDTO(any())).thenReturn(updatedUserData);

            mockMvc.perform(patch(BASE_PATH + "/update-profile")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerUserDTO))
                            .param(user.getPassword(), customerUserDTO.getPassword()))
                    .andExpect(status().isOk());


            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<String> userEmailCaptor = ArgumentCaptor.forClass(String.class);
            verify(userService, times(1)).updateUserData(userArgumentCaptor.capture(), userEmailCaptor.capture());

            assertEquals(updatedUserData, userArgumentCaptor.getValue());
            assertEquals(user.getEmail(), userEmailCaptor.getValue());
        } else {
            String password = user == null ? "" : user.getPassword();

            mockMvc.perform(patch(BASE_PATH + "/update-profile")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerUserDTO))
                    )
                    .andExpect(status().isUnauthorized());

            verify(userService, times(0)).updateUserData(user, password);
        }
    }

    private static Stream<Arguments> provideUser() {
        return Stream.of(
                Arguments.of(someCustomerUserModel1(), true),
                Arguments.of(someBusinessUserModel1(), false),
                Arguments.of(null, false)
        );
    }
}