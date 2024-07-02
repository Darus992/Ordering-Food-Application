package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.model.Address;

import java.util.List;

@UtilityClass
public class AddressFixtures {

    public static Address someAddressModel1() {
        return Address.builder()
                .city("Białystok")
                .district("Antoniuk")
                .postalCode("12-221")
                .addressStreet("Antoniukowska 100")
                .build();
    }

    public static Address someAddressModel2() {
        return Address.builder()
                .city("Warszawa")
                .district("Testowy")
                .postalCode("22-220")
                .addressStreet("Kolejna ulica 100")
                .build();
    }

    public static Address someAddressModel3() {
        return Address.builder()
                .city("Kraków")
                .district("Olsza")
                .postalCode("22-123")
                .addressStreet("Orszakowa 78")
                .build();
    }

    public static Address someAddressModel4() {
        return Address.builder()
                .city("Warszawa")
                .district("Zacisze")
                .postalCode("11-253")
                .addressStreet("Lisia 2")
                .build();
    }

    public static Address someErrorAddressModel2() {
        return Address.builder()
                .city("")
                .district("Testowy")
                .postalCode("")
                .addressStreet("Kolejna ulica 100")
                .build();
    }

    public static AddressEntity someAddressEntity1() {
        return AddressEntity.builder()
                .city("Białystok")
                .district("Antoniuk")
                .postalCode("12-221")
                .address("Antoniukowska 100")
                .build();
    }

    public static AddressEntity someAddressEntity2() {
        return AddressEntity.builder()
                .city("Warszawa")
                .district("Testowy")
                .postalCode("22-220")
                .address("Kolejna ulica 100")
                .build();
    }

    public static AddressEntity someAddressEntity3() {
        return AddressEntity.builder()
                .city("Kraków")
                .district("Olsza")
                .postalCode("22-123")
                .address("Orszakowa 78")
                .build();
    }

    public static AddressEntity someAddressEntity4() {
        return AddressEntity.builder()
                .city("Warszawa")
                .district("Zacisze")
                .postalCode("11-253")
                .address("Lisia 2")
                .build();
    }

    public static List<AddressEntity> someListOfAddressEntity1() {
        return List.of(someAddressEntity1());
    }

    public static List<AddressEntity> someListOfAddressEntity2() {
        return List.of(someAddressEntity2(), someAddressEntity4());
    }
}
