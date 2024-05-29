package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;

@UtilityClass
public class CustomerRequestFormFixtures {

    public static CustomerRequestForm someCustomerRequestForm() {
        CustomerRequestForm customerRequestForm = new CustomerRequestForm();
        customerRequestForm.setUsername("testowy_customer");
        customerRequestForm.setUserEmail("testowy_customer@mail.com");
        customerRequestForm.setUserPassword("haslo");
        customerRequestForm.setCustomerName("Leszek");
        customerRequestForm.setCustomerSurname("Zaradny");
        customerRequestForm.setCustomerPhone("154457147");
        customerRequestForm.setCustomerAddressCity("Warszawa");
        customerRequestForm.setCustomerAddressDistrict("Testowy");
        customerRequestForm.setCustomerAddressPostalCode("22-220");
        customerRequestForm.setCustomerAddressStreet("Kolejna ulica 100");
        return customerRequestForm;
    }
//    public static CustomerRequestForm someCustomerRequestForm() {
//        CustomerRequestForm customerRequestForm = new CustomerRequestForm();
//        customerRequestForm.setUsername("testowy_Nickname");
//        customerRequestForm.setUserEmail("testowy_Nickname@mail.com");
//        customerRequestForm.setUserPassword("haslo");
//        customerRequestForm.setCustomerName("Leszek");
//        customerRequestForm.setCustomerSurname("Zaradny");
//        customerRequestForm.setCustomerPhone("154457147");
//        customerRequestForm.setCustomerAddressCity("Warszawa");
//        customerRequestForm.setCustomerAddressDistrict("Testowy");
//        customerRequestForm.setCustomerAddressPostalCode("22-220");
//        customerRequestForm.setCustomerAddressStreet("Kolejna ulica 100");
//        return customerRequestForm;
//    }
}
