package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.model.Customer;

import static pl.dariuszgilewicz.util.AddressFixtures.*;

@UtilityClass
public class CustomerFixtures {

    public static CustomerEntity someCustomerEntity1() {
        return CustomerEntity.builder()
                .name("Leszek")
                .surname("Zaradny")
                .phone("154457147")
                .address(someAddressEntity2())
                .build();
    }

    public static CustomerEntity someCustomerEntity2() {
        return CustomerEntity.builder()
                .name("Robert")
                .surname("Zaradny")
                .phone("112457140")
                .address(someAddressEntity4())
                .build();
    }

    public static Customer someCustomerModel1() {
        return Customer.builder()
                .name("Leszek")
                .surname("Zaradny")
                .phone("154457147")
                .address(someAddressModel2())
                .build();
    }

    public static Customer someCustomerModel2() {
        return Customer.builder()
                .name("Robert")
                .surname("Zaradny")
                .phone("112457140")
                .address(someAddressModel4())
                .build();
    }

    public static Customer someErrorCustomerModel1() {
        return Customer.builder()
                .name("Leszek")
                .surname("Zaradny")
                .phone("1457S147K")
                .address(someAddressModel2())
                .build();
    }

    public static Customer someErrorCustomerModel2() {
        return Customer.builder()
                .name("")
                .surname("")
                .phone("1457147123456")
                .address(someErrorAddressModel2())
                .build();
    }
}
