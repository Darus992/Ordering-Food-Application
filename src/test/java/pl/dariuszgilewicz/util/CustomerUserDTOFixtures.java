package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.api.dto.CustomerUserDTO;

@UtilityClass
public class CustomerUserDTOFixtures {

    public static CustomerUserDTO someCustomerUserDTO1() {
        CustomerUserDTO customerUserDTO = CustomerUserDTO.builder()
                .customerName("Leszek")
                .customerSurname("Zaradny")
                .customerPhone("154457147")
                .addressCity("Warszawa")
                .addressDistrict("Testowy")
                .addressPostalCode("22-220")
                .addressStreet("Kolejna ulica 100")
                .build();
        customerUserDTO.setUsername("testowy_customer");
        customerUserDTO.setEmail("testowy_customer@mail.com");
        customerUserDTO.setPassword("$2a$10$AmdT3YzpxiS18q5SMmB7DuWU1v7zrS1.1IORgg9YPw6/r/3CapSqy");
        return customerUserDTO;
    }
}
