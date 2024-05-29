package pl.dariuszgilewicz.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.security.UserService;

@Controller
@AllArgsConstructor
@RequestMapping(UserRegistrationController.USER)
public class UserRegistrationController {

    public static final String USER = "/user";
    private final UserService userService;


    @GetMapping("/register-customer")
    public String showCustomerRegistrationForm(Model model) {
        model.addAttribute("customerRequestForm", new CustomerRequestForm());
        return "customer_form";
    }

    @GetMapping("/register-business")
    public String showBusinessRegistrationForm(Model model) {
        model.addAttribute("businessRequestForm", new BusinessRequestForm());
        return "business_form";
    }

    @PostMapping("/register-customer")
    public String createCustomerUserForm(@ModelAttribute("customerRequestForm") CustomerRequestForm customerRequestForm) {
        userService.createCustomerUser(customerRequestForm);
        return "redirect:/";
    }

    @PostMapping("/register-business")
    public String createBusinessUserForm(
            @Valid @ModelAttribute("businessRequestForm") BusinessRequestForm businessRequestForm,
            BindingResult bindingResult
    ) {
        if (businessRequestForm.getRestaurantImageCard().isEmpty()) {
            bindingResult.addError(new FieldError("businessRequestForm", "restaurantImageCard", "The image file is required."));
            bindingResult.addError(new FieldError("businessRequestForm", "restaurantImageHeader", "The image file is required."));
        }

        if (bindingResult.hasErrors()) {
            return "business_form";
        }
        userService.createBusinessUser(businessRequestForm);
        return "redirect:/";
    }

}
