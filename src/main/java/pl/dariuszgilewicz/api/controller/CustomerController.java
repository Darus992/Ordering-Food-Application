package pl.dariuszgilewicz.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(CustomerController.CUSTOMER)
public class CustomerController {

    public static final String CUSTOMER = "/customer";
    private UserService userService;

    @GetMapping
    public String showCustomerPage(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "customer";
    }
}
