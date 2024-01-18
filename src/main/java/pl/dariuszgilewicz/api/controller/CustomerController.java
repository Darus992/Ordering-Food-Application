package pl.dariuszgilewicz.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping(CustomerController.CUSTOMER)
public class CustomerController {

    public static final String CUSTOMER = "/customer";

    @GetMapping
    public String showCustomerPage() {
        return "customer";
    }
}
