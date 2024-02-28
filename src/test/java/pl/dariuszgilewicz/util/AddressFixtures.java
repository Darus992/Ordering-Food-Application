package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;

import java.util.List;

@UtilityClass
public class AddressFixtures {

    public static List<AddressEntity> someListOfAddressEntity1() {
        return List.of(AddressEntity.builder()
                        .city("Białystok")
                        .district("Antoniuk")
                        .postalCode("12-221")
                        .address("Antoniukowska 100")
                        .build(),
                AddressEntity.builder()
                        .city("Warszawa")
                        .district("Zacisze")
                        .postalCode("11-253")
                        .address("Lisia 2")
                        .build()
        );
    }
    public static List<AddressEntity> someListOfAddressEntity2() {
        return List.of(AddressEntity.builder()
                        .city("Białystok")
                        .district("Młodych")
                        .postalCode("10-540")
                        .address("Nazwa ulicy 3")
                        .build(),
                AddressEntity.builder()
                        .city("Gdańsk")
                        .district("Testowa")
                        .postalCode("16-503")
                        .address("Długa 15")
                        .build(),
                AddressEntity.builder()
                        .city("Gdańsk")
                        .district("Dzielna")
                        .postalCode("26-181")
                        .address("Mostowa 255")
                        .build()
        );
    }
}
