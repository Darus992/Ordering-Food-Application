package pl.dariuszgilewicz.api.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping(CustomerController.CUSTOMER)
public class CustomerController {

    public static final String CUSTOMER = "/customer";
    private UserService userService;

    @GetMapping
    public String showCustomerPage(Model model) {
        User user = getCurrentUser(model);
        model.addAttribute("user", user);
        model.addAttribute("userForm", new User());
        model.addAttribute("defaultTab", "list-profile-list");
        return "customer";
    }

    @PatchMapping("/update-profile")
    public String updateCustomerDetailsPage(
            @Valid @ModelAttribute("userForm") User userForm,
            @RequestParam(name = "emailParam") String userEmail,
            BindingResult bindingResult,
            Model model
    ) {
        if (checkIfHasErrors(userForm, bindingResult, model)) {
            model.addAttribute("defaultTab", "list-profile-list");
            return "customer";
        }
        userService.updateUserData(userForm, userEmail);
        return "redirect:/customer";
    }

    @DeleteMapping("/delete-account")
    public String deleteCustomerAccountPage(@RequestParam("userEmail") String userEmail) {
        userService.deleteAccount(userEmail);
        return "redirect:/logout";
    }

    private User getCurrentUser(Model model) {
        Optional<User> optional = userService.getCurrentOptionalUser(model);
        return optional.orElseThrow(
                () -> new EntityNotFoundException("User: [%s] not found.".formatted(optional))
        );
    }

    private boolean checkIfHasErrors(User userForm, BindingResult bindingResult, Model model) {
        validate(bindingResult, userForm);

        if (bindingResult.hasErrors()) {
            User currentUser = getCurrentUser(model);
            model.addAttribute("user", currentUser);
            model.addAttribute("userForm", userForm);
            return true;
        }
        return false;
    }

    private static void validate(BindingResult bindingResult, User userForm) {
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "username", "required.username", "Username is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "email", "required.email", "User email is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "customer.phone", "required.customer.phone", "Phone is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "customer.name", "required.name", "Name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "customer.surname", "required.surname", "Surname is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "customer.address.addressStreet", "required.address.addressStreet", "Street is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "customer.address.city", "required.address.city", "City is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "customer.address.district", "required.address.district", "District is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "customer.address.postalCode", "required.address.postalCode", "Postal code is required.");

        if (userForm.getCustomer().getPhone().length() != 9) {
            bindingResult.addError(new FieldError("userForm", "customer.phone", "The phone number size cannot be larger or smaller than 9."));
        }

        if (!userForm.getCustomer().getPhone().matches("^\\d+$")) {
            bindingResult.addError(new FieldError("userForm", "customer.phone", "Phone number must contain only digits."));
        }

        if (userForm.getUsername().length() < 5) {
            bindingResult.addError(new FieldError("userForm", "username", "Username must contain at least 5 characters."));
        }

        if (!userForm.getPassword().isEmpty() && userForm.getPassword().length() < 5) {
            bindingResult.addError(new FieldError("userForm", "password", "Password must contain at least 5 characters."));
        }
    }
}
