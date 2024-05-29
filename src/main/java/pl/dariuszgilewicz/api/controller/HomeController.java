package pl.dariuszgilewicz.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.Optional;


@Controller
@AllArgsConstructor
public class HomeController {

    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        Optional<User> optionalUser = userService.getCurrentOptionalUser(model);
        optionalUser.ifPresent(user -> model.addAttribute("user", user));
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
