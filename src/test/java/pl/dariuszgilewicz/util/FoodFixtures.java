package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.model.Food;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class FoodFixtures {

    public static List<FoodEntity> someFoodEntityList1() {
        return List.of(someFoodEntity1());
    }

    public static List<FoodEntity> someFoodEntityList2() {
        return new ArrayList<>(List.of(
                someFoodEntity1(),
                someFoodEntity2(),
                someFoodEntity3(),
                someFoodEntity4()
        ));
    }

    public static FoodEntity someFoodEntity1() {
        return FoodEntity.builder()
                .category("Pizza")
                .name("Super Pizza")
                .description("dodatki, sosy, itp.")
                .price(BigDecimal.valueOf(35.5))
                .build();
    }

    public static FoodEntity someFoodEntity2() {
        return FoodEntity.builder()
                .category("Pizza")
                .name("Mega Pizza")
                .description("dodatki, sosy, itp.")
                .price(BigDecimal.valueOf(50L))
                .build();
    }

    public static FoodEntity someFoodEntity3() {
        return FoodEntity.builder()
                .category("Napoje")
                .name("Pepsi")
                .description("")
                .price(BigDecimal.valueOf(6L))
                .build();
    }

    public static FoodEntity someFoodEntity4() {
        return FoodEntity.builder()
                .category("Napoje")
                .name("Cola")
                .description("")
                .price(BigDecimal.valueOf(6L))
                .build();
    }

    public static Food someFood1() {
        return Food.builder()
                .category("Pizza")
                .name("Super Pizza")
                .description("dodatki, sosy, itp.")
                .price(BigDecimal.valueOf(35.5))
                .build();
    }
}
