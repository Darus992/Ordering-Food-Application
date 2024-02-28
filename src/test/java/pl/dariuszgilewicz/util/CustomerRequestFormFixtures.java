package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;

@UtilityClass
public class CustomerRequestFormFixtures {

    public static CustomerRequestForm someCustomerRequestForm() {
        return CustomerRequestForm.builder()
                .username("testowy_Nickname")
                .userEmail("testowy_Nickname@mail.com")
                .userPassword("haslo")
                .customerName("Leszek")
                .customerSurname("Zaradny")
                .customerPhone("154457147")
                .customerAddressCity("Warszawa")
                .customerAddressDistrict("Testowy")
                .customerAddressPostalCode("22-220")
                .customerAddressStreet("Kolejna ulica 100")
                .build();
    }
}
