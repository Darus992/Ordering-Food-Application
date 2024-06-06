package pl.dariuszgilewicz.api.dto.mapper;

import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.api.dto.CustomerUserDTO;
import pl.dariuszgilewicz.infrastructure.model.Address;
import pl.dariuszgilewicz.infrastructure.model.Customer;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserRole;

@Component
public class CustomerUserMapper {

    public CustomerUserDTO mapToDTO(User user) {
        CustomerUserDTO customerUserDTO = CustomerUserDTO.builder()
                .customerName(user.getCustomer().getName())
                .customerSurname(user.getCustomer().getSurname())
                .customerPhone(user.getCustomer().getPhone())
                .addressCity(user.getCustomer().getAddress().getCity())
                .addressDistrict(user.getCustomer().getAddress().getDistrict())
                .addressPostalCode(user.getCustomer().getAddress().getPostalCode())
                .addressStreet(user.getCustomer().getAddress().getAddressStreet())
                .build();

        customerUserDTO.setUsername(user.getUsername());
        customerUserDTO.setEmail(user.getEmail());

        return customerUserDTO;
    }

    public User mapFromDTO(CustomerUserDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .role(UserRole.CUSTOMER)
                .customer(Customer.builder()
                        .name(dto.getCustomerName())
                        .surname(dto.getCustomerSurname())
                        .phone(dto.getCustomerPhone())
                        .address(Address.builder()
                                .city(dto.getAddressCity())
                                .district(dto.getAddressDistrict())
                                .postalCode(dto.getAddressPostalCode())
                                .addressStreet(dto.getAddressStreet())
                                .build())
                        .build())
                .build();
    }

    public CustomerRequestForm mapFromDTOToCustomerRequestForm(CustomerUserDTO dto) {
        CustomerRequestForm customerRequestForm = CustomerRequestForm.builder()
                .customerName(dto.getCustomerName())
                .customerSurname(dto.getCustomerSurname())
                .customerPhone(dto.getCustomerPhone())
                .customerAddressCity(dto.getAddressCity())
                .customerAddressDistrict(dto.getAddressDistrict())
                .customerAddressPostalCode(dto.getAddressPostalCode())
                .customerAddressStreet(dto.getAddressStreet())
                .build();

        customerRequestForm.setUsername(dto.getUsername());
        customerRequestForm.setUserEmail(dto.getEmail());
        customerRequestForm.setUserPassword(dto.getPassword());

        return customerRequestForm;
    }
}
