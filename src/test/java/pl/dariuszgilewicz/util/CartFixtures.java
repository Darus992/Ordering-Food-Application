package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import pl.dariuszgilewicz.infrastructure.model.Cart;
import pl.dariuszgilewicz.infrastructure.model.Food;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static pl.dariuszgilewicz.util.FoodFixtures.someFoodModel4;

@UtilityClass
public class CartFixtures {

    public static Cart someCartModelSummary1() {
        Map<Food, Integer> cartRequest = createCartRequest1();

        return Cart.builder()
                .cartRequest(cartRequest)
                .totalPrice(BigDecimal.valueOf(21.98))
                .build();
    }

    public static Cart someCartModel1() {
        Map<Food, Integer> cartRequest = createCartRequest1();
        Integer[] foodsId = {1};
        Integer[] foodsValue = {2};

        return Cart.builder()
                .cartRequest(cartRequest)
                .foodsIdArray(foodsId)
                .foodsValuesArray(foodsValue)
                .totalPrice(BigDecimal.valueOf(21.98))
                .orderNotes("The doorbell doesn't work, please give me a call.")
                .build();
    }

    private static Map<Food, Integer> createCartRequest1() {
        Map<Food, Integer> cartRequest = new HashMap<>();
        Food food = someFoodModel4();

        cartRequest.put(food, 2);

        return cartRequest;
    }
}
