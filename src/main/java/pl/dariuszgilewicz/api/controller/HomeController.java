package pl.dariuszgilewicz.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.dariuszgilewicz.infrastructure.security.UserService;


@Controller
@AllArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        boolean isAuthenticated = UserService.checkIfIsAuthenticated(model, authentication);
        UserService.assignRoleDependsOnAuthentication(authentication, model, isAuthenticated);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
