package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

import java.util.List;

import static pl.dariuszgilewicz.util.FoodFixtures.*;

@UtilityClass
public class FoodMenuFixtures {

    public static List<FoodMenuEntity> someListOfFoodMenuEntities1() {
        return List.of(someFoodMenuEntity1());
    }

    public static List<FoodMenu> someListOfFoodMenu() {
        return List.of(someFoodMenuModel1());
    }

    public static FoodMenu someFoodMenuModel1() {
        return FoodMenu.builder()
                .foodMenuId(1)
                .foods(someFoodModelList2())
                .build();
    }

    public static FoodMenu someFoodMenuModel2() {
        return FoodMenu.builder()
                .foodMenuId(2)
                .foods(List.of(someFoodModel1(), someFoodModel3()))
                .build();
    }

    public static FoodMenu someFoodMenuModel3() {
        return FoodMenu.builder()
                .foodMenuId(3)
                .foods(someFoodModelList3())
                .build();
    }

    public static FoodMenu someFoodMenuModel4() {
        return FoodMenu.builder()
                .foodMenuName("Testowa Nazwa")
                .foods(someFoodModelList4())
                .build();
    }

    public static FoodMenuEntity someFoodMenuEntity1() {
        return FoodMenuEntity.builder()
                .foodMenuId(1)
                .foods(someFoodEntityList2())
                .build();
    }

    public static FoodMenuEntity someFoodMenuEntity2() {
        return FoodMenuEntity.builder()
                .foodMenuId(2)
                .foods(someFoodEntityList1())
                .build();
    }

    public static FoodMenuEntity someFoodMenuEntity3() {
        return FoodMenuEntity.builder()
                .foodMenuId(3)
                .foods(someFoodEntityList3())
                .build();
    }

    public static FoodMenuEntity someFoodMenuEntity4() {
        return FoodMenuEntity.builder()
                .menuName("Testowa Nazwa")
                .foods(someFoodEntityList4())
                .build();
    }

    public static FoodMenuEntity someFoodMenuEntity5() {
        return FoodMenuEntity.builder()
                .menuName("Przypisane Menu")
                .foods(someFoodEntityList5())
                .build();
    }
}
