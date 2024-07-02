package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockMultipartFile;
import pl.dariuszgilewicz.api.dto.FoodDTO;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.model.Food;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class FoodFixtures {

    public static List<FoodEntity> someFoodEntityList1() {
        return new ArrayList<>(List.of(someFoodEntity1(), someFoodEntity3()));
    }

    public static List<FoodEntity> someFoodEntityList2() {
        return new ArrayList<>(List.of(
                someFoodEntity1(),
                someFoodEntity2(),
                someFoodEntity3(),
                someFoodEntity4()
        ));
    }

    public static List<FoodEntity> someFoodEntityList3() {
        return new ArrayList<>(List.of(someFoodEntity3(), someFoodEntity4()));
    }

    public static List<FoodEntity> someFoodEntityList4() {
        return new ArrayList<>(List.of(someFoodEntity5()));
    }

    public static List<FoodEntity> someFoodEntityList5() {
        return new ArrayList<>(List.of(someFoodEntity6()));
    }

    public static List<FoodEntity> someFoodEntityList6() {
        return new ArrayList<>(List.of(someFoodEntity1(), someFoodEntity2(), someFoodEntity3()));
    }

    public static List<Food> someFoodModelList1() {
        return new ArrayList<>(List.of(someFoodModel1(), someFoodModel3()));
    }

    public static List<Food> someFoodModelList2() {
        return new ArrayList<>(List.of(
                someFoodModel1(),
                someFoodModel2(),
                someFoodModel3(),
                someFoodModel4()
        ));
    }

    public static List<Food> someFoodModelList3() {
        return new ArrayList<>(List.of(someFoodModel3(), someFoodModel4()));
    }

    public static List<Food> someFoodModelList4() {
        return new ArrayList<>(List.of(someFoodModel5()));
    }

    public static Map<Food, Integer> someFoodsModelMap1() {
        Map<Food, Integer> foodIntegerMap = new HashMap<>();
        foodIntegerMap.put(someFoodModel1(), 1);
        foodIntegerMap.put(someFoodModel2(), 2);
        foodIntegerMap.put(someFoodModel3(), 3);
        return foodIntegerMap;
    }

    public static Map<FoodEntity, Integer> someFoodsEntityMap1() {
        Map<FoodEntity, Integer> foodIntegerMap = new HashMap<>();
        foodIntegerMap.put(someFoodEntity1(), 1);
        foodIntegerMap.put(someFoodEntity2(), 2);
        foodIntegerMap.put(someFoodEntity3(), 3);
        return foodIntegerMap;
    }

    public static Map<FoodEntity, Integer> someFoodsEntityMap2() {
        Map<FoodEntity, Integer> foodIntegerMap = new HashMap<>();
        foodIntegerMap.put(someFoodEntity5(), 1);
        return foodIntegerMap;
    }

    public static FoodEntity someFoodEntity1() {
        return FoodEntity.builder()
                .foodId(4)
                .category("Pizza")
                .name("Super Pizza")
                .description("dodatki, sosy, itp.")
                .price(BigDecimal.valueOf(35.5))
                .build();
    }

    public static FoodEntity someFoodEntity2() {
        return FoodEntity.builder()
                .foodId(2)
                .category("Pizza")
                .name("Mega Pizza")
                .description("dodatki, sosy, itp.")
                .price(BigDecimal.valueOf(50L))
                .build();
    }

    public static FoodEntity someFoodEntity3() {
        return FoodEntity.builder()
                .foodId(3)
                .category("Napoje")
                .name("Pepsi")
                .description("")
                .price(BigDecimal.valueOf(6L))
                .build();
    }

    public static FoodEntity someFoodEntity4() {
        return FoodEntity.builder()
                .foodId(1)
                .category("Napoje")
                .name("Cola")
                .description("")
                .price(BigDecimal.valueOf(6L))
                .build();
    }

    public static FoodEntity someFoodEntity5() {
        byte[] content = {84, 101, 115, 116, 32, 99, 111, 110, 116, 101, 110, 116};

        return FoodEntity.builder()
                .foodImage(content)
                .category("Napoje")
                .name("Pepsi Zero")
                .description("")
                .price(BigDecimal.valueOf(6.5))
                .build();
    }

    public static FoodEntity someFoodEntity6() {
        byte[] content = {84, 101, 115, 116, 32, 99, 111, 110, 116, 101, 110, 116};

        return FoodEntity.builder()
                .foodImage(content)
                .category("Pizza")
                .name("Mega Pizza")
                .description("dodatki, sosy, itp.")
                .price(BigDecimal.valueOf(50L))
                .build();
    }

    public static Food someFoodModel1() {
        return Food.builder()
                .foodId(4)
                .category("Pizza")
                .name("Super Pizza")
                .description("dodatki, sosy, itp.")
                .price(BigDecimal.valueOf(35.5))
                .build();
    }

    public static Food someFoodModel2() {
        return Food.builder()
                .foodId(2)
                .category("Pizza")
                .name("Mega Pizza")
                .description("dodatki, sosy, itp.")
                .price(BigDecimal.valueOf(50L))
                .build();
    }

    public static Food someFoodModel3() {
        return Food.builder()
                .foodId(3)
                .category("Napoje")
                .name("Pepsi")
                .description("")
                .price(BigDecimal.valueOf(6L))
                .build();
    }

    public static Food someFoodModel4() {
        return Food.builder()
                .foodId(1)
                .name("Pizza")
                .category("Pizza")
                .description("Delicious pizza")
                .price(BigDecimal.valueOf(10.99))
                .build();
    }

    public static Food someFoodModel5() {
        byte[] content = "Test content".getBytes();

        return Food.builder()
                .category("Napoje")
                .name("Pepsi Zero")
                .description("")
                .price(BigDecimal.valueOf(6.5))
                .fileImageToUpload(new MockMultipartFile("pepsiZero.jpg", content))
                .build();
    }

    public static FoodDTO someFoodDTO1() {
        return FoodDTO.builder()
                .foodImage("http://localhost:8190/ordering-food-application/image/food/4")
                .foodCategory("Pizza")
                .foodName("Super Pizza")
                .foodDescription("dodatki, sosy, itp.")
                .foodPrice("35.5")
                .build();
    }

    public static FoodDTO someFoodDTO2() {
        return FoodDTO.builder()
                .foodImage("http://localhost:8190/ordering-food-application/image/food/2")
                .foodCategory("Pizza")
                .foodName("Mega Pizza")
                .foodDescription("dodatki, sosy, itp.")
                .foodPrice("50")
                .build();
    }

    public static FoodDTO someFoodDTO3() {
        return FoodDTO.builder()
                .foodImage("http://localhost:8190/ordering-food-application/image/food/3")
                .foodCategory("Napoje")
                .foodName("Pepsi")
                .foodDescription("")
                .foodPrice("6")
                .build();
    }
}
