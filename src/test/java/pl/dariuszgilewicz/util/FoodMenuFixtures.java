package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class FoodMenuFixtures {

    public static List<FoodMenuEntity> someListOfFoodMenuEntities1(){
        return List.of(someFoodMenuEntity1());
    }

    public static List<FoodMenu> someListOfFoodMenu(){
        return List.of(someFoodMenu1());
    }

    public static FoodMenuEntity someFoodMenuEntity1(){
        return FoodMenuEntity.builder()
                .menuName("Restauracje_1")
                .foods(List.of(FoodEntity.builder()
                                .category("Pizza")
                                .name("Pizza nr 1")
                                .description("dodatki, sosy, itp.")
                                .price(BigDecimal.valueOf(35L))
                        .build(),
                        FoodEntity.builder()
                                .category("Pizza")
                                .name("Pizza nr 2")
                                .description("dodatki, sosy, itp.")
                                .price(BigDecimal.valueOf(40.5))
                                .build(),
                        FoodEntity.builder()
                                .category("Burgery")
                                .name("Super burger")
                                .description("Warzywa, sosy")
                                .price(BigDecimal.valueOf(18L))
                                .build(),
                        FoodEntity.builder()
                                .category("Napoje")
                                .name("Pepsi")
                                .description("")
                                .price(BigDecimal.valueOf(6L))
                                .build()
                ))
                .build();
    }

    public static FoodMenu someFoodMenu1(){
        return FoodMenu.builder()
                .foodMenuName("Restauracje_1")
                .foods(List.of(Food.builder()
                                .category("Pizza")
                                .name("Pizza nr 1")
                                .description("dodatki, sosy, itp.")
                                .price(BigDecimal.valueOf(35L))
                                .build(),
                        Food.builder()
                                .category("Pizza")
                                .name("Pizza nr 2")
                                .description("dodatki, sosy, itp.")
                                .price(BigDecimal.valueOf(40.5))
                                .build(),
                        Food.builder()
                                .category("Burgery")
                                .name("Super burger")
                                .description("Warzywa, sosy")
                                .price(BigDecimal.valueOf(18L))
                                .build(),
                        Food.builder()
                                .category("Napoje")
                                .name("Pepsi")
                                .description("")
                                .price(BigDecimal.valueOf(6L))
                                .build()
                ))
                .build();
    }

    public static FoodMenu someFoodMenu2(){
        return FoodMenu.builder()
                .foodMenuName("Przykładowa nazwa")
//                .foodCategory("Pizza")
//                .foodName("Super Pizza")
//                .foodDescription("dodatki, sosy, itp.")
//                .foodPrice(BigDecimal.valueOf(35.5))
                .foods(new ArrayList<>())
                .build();
    }

    public static FoodMenuEntity someFoodMenuEntity2(){
        return FoodMenuEntity.builder()
                .menuName("Przykładowa nazwa")
                .foods(FoodFixtures.someFoodEntityList1())
                .build();
    }

    public static FoodMenuEntity someFoodMenuEntity3(){
        return FoodMenuEntity.builder()
                .menuName("Przykładowa nazwa")
                .foods(FoodFixtures.someFoodEntityList2())
                .build();
    }
}
