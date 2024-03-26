package pl.dariuszgilewicz.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.dariuszgilewicz.business.OrderService;
import pl.dariuszgilewicz.infrastructure.database.enums.OrderStatus;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.List;

@Controller
@AllArgsConstructor
public class OrdersController {

    private OrderService orderService;
    private UserService userService;
    private OrderRepository orderRepository;

    @GetMapping("/cart")
    public String showCartDetails(
            @RequestParam(required = false) Integer orderNumber,
            Model model
    ) {
        Orders order = orderRepository.findOrderByOrderNumber(orderNumber);
        User user = userService.getCurrentUser();
        model.addAttribute("order", order);
        model.addAttribute("user", user);
        return "cart_details";
    }

    @GetMapping("/order/{orderNumber}")
    public String showOrder(
            @PathVariable Integer orderNumber,
            Model model
    ) {
        Orders order = orderRepository.findOrderByOrderNumber(orderNumber);
        User user = userService.getCurrentUser();

        if(!order.getStatus().equals(OrderStatus.COMPLETED)) {
            orderService.checkForUpdateStatus(order);
        }

        model.addAttribute("order", order);
        model.addAttribute("user", user);
        return "order_details";
    }

    @PostMapping("/create-cart")
    public String createCart(
            @RequestParam List<Integer> foodIds,
            @RequestParam List<Integer> quantity,
            @RequestParam String restaurantEmail
    ) {
        String username = userService.getCurrentUserName();
        orderService.createCart(foodIds, quantity, username, restaurantEmail);
        return "redirect:/restaurant/" + restaurantEmail;
    }

    @PutMapping("/create-order")
    public String createOrder(
            @RequestParam Integer orderNumber,
            @ModelAttribute Orders order,
            Model model
    ) {
        model.addAttribute("orderNumber", orderNumber);
        orderService.createOrder(orderNumber, order);
        return "redirect:/order/" + orderNumber;
    }

    @PutMapping("/update-cart")
    public String updateOrdersCart(
            @RequestParam List<Integer> foodIds,
            @RequestParam List<Integer> quantity,
            @RequestParam("restaurantEmail") String restaurantEmail,
            @RequestParam Integer orderNumber
    ) {
        orderService.updateCart(orderNumber, foodIds, quantity);
        return "redirect:/restaurant/" + restaurantEmail;
    }

    @DeleteMapping("/delete-food-from-cart")
    public String deleteFoodFromOrderCart(
            @RequestParam Integer orderNumber,
            @RequestParam Integer foodId,
            @RequestParam Integer quantity
    ) {
        boolean isDeleteCompletely = orderService.deleteFoodFromCart(orderNumber, foodId, quantity);
        if (isDeleteCompletely) {
            return "redirect:/customer";
        } else {
            return "redirect:/cart?orderNumber=" + orderNumber;
        }
    }

    @DeleteMapping("/delete-cart")
    public String deleteCart(@RequestParam Integer orderNumber) {
        orderService.deleteCart(orderNumber);
        return "redirect:/customer";
    }
}
