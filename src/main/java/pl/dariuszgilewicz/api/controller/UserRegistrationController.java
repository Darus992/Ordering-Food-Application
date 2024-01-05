package pl.dariuszgilewicz.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dariuszgilewicz.infrastructure.security.UserService;
import pl.dariuszgilewicz.infrastructure.security.User;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserRegistrationController {

    public static final String REGISTRATION = "/register";
    private final UserService userService;


    @GetMapping(value = REGISTRATION)
    public String showRegistrationForm(Model model){
        model.addAttribute("userForm", new User());
        return "user_registration_form";
    }

    @PostMapping(value = REGISTRATION)
    public String registerUser(@ModelAttribute("userForm") User user){
        userService.createUser(user);
        return "redirect:/owner/create";
    }

}
