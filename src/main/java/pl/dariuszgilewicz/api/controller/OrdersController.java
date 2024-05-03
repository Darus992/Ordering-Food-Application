package pl.dariuszgilewicz.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.dariuszgilewicz.business.OrderService;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.model.Cart;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class OrdersController {

    private OrderService orderService;
    private UserService userService;
    private OrderRepository orderRepository;

    @GetMapping("/order/cart-summary")
    public String showCartSummary(
            @RequestParam("foodKeys") String foodKeysJSON,
            @RequestParam("foodValues") String foodValuesJSON,
            @RequestParam("totalPrice") BigDecimal totalPrice,
            @RequestParam("restaurantEmail") String restaurantEmail,
            Model model
    ) {
        findAndAddUserToModelAttributeIfIsAuthenticated(model);
        model.addAttribute("restaurantEmail", restaurantEmail);
        mapJsonToObjectValues(null, foodValuesJSON, foodKeysJSON, totalPrice, model);
        return "cart_details";
    }

    @GetMapping("/order/{orderNumber}/details")
    public String showOrderDetails(@PathVariable Integer orderNumber, Model model) {
        findAndAddUserToModelAttributeIfIsAuthenticated(model);
        Orders order = orderRepository.findOrderByOrderNumber(orderNumber);
        model.addAttribute("order", order);
        return "order_details";
    }

    @PostMapping("/order/create-order")
    public String createOrder(
            @RequestParam("foodsId") String jsonFoodsId,
            @RequestParam("foodsValues") String jsonFoodsValues,
            @RequestParam("totalPrice") BigDecimal totalPrice,
            @RequestParam("orderNotes") String orderNotes,
            @RequestParam("restaurantEmail") String restaurantEmail,
            Model model
    ) {
        findAndAddUserToModelAttributeIfIsAuthenticated(model);
        User user = (User) model.getAttribute("user");
        Cart cart = mapJsonToObjectValues(jsonFoodsId, jsonFoodsValues, null, totalPrice, model);

        if (user != null) {
            Integer orderNumber = orderService.createOrderAndReturnOrderNumber(cart.getFoodsIdArray(), cart.getFoodsValuesArray(), totalPrice, orderNotes, user, restaurantEmail);
            return "redirect:/order/" + orderNumber + "/details";
        }
        return "login";
    }

    private Cart mapJsonToObjectValues(String jsonFoodsId, String jsonFoodsValues, String jsonFoodsKeys, BigDecimal totalPrice, Model model) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Food, Integer> cartMap = new HashMap<>();
        Cart cart = new Cart();
        Integer[] foodsValues;

        try {
            foodsValues = objectMapper.readValue(jsonFoodsValues, Integer[].class);
            Integer[] foodsId = new Integer[foodsValues.length];

            if (jsonFoodsKeys != null) {
                Food[] cartFoods = objectMapper.readValue(jsonFoodsKeys, Food[].class);
                for (int i = 0; i < cartFoods.length; i++) {
                    Food food = Food.builder()
                            .foodId(cartFoods[i].getFoodId())
                            .category(cartFoods[i].getCategory())
                            .name(cartFoods[i].getName())
                            .description(cartFoods[i].getDescription())
                            .price(cartFoods[i].getPrice())
                            .build();
                    cartMap.put(food, foodsValues[i]);
                    foodsId[i] = cartFoods[i].getFoodId();
                }
                cart.setCartRequest(cartMap);
                cart.setTotalPrice(totalPrice);

                model.addAttribute("cart", cart);
                model.addAttribute("foodsId", Arrays.asList(foodsId));
                model.addAttribute("foodsValues", Arrays.asList(foodsValues));
            }

            if (jsonFoodsId != null) {
                foodsId = objectMapper.readValue(jsonFoodsId, Integer[].class);
                cart.setFoodsIdArray(foodsId);
                cart.setFoodsValuesArray(foodsValues);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return cart;
    }

    private void findAndAddUserToModelAttributeIfIsAuthenticated(Model model) {
        Optional<User> optionalUser = userService.getCurrentOptionalUser(model);
        optionalUser.ifPresent(user -> model.addAttribute("user", user));
    }
}
