package pl.dariuszgilewicz.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

@Controller
@AllArgsConstructor
@RequestMapping(RestaurantOwnerController.OWNER)
public class RestaurantOwnerController {

    public static final String OWNER = "/owner";
    private UserService userService;

    @GetMapping
    public String showOwnerPage(Model model) {
        String userName = userService.getCurrentUserName();
        User user = userService.findUserByUserName(userName);
//        model.addAttribute("isCompleted", user.getIsCompleted());
        model.addAttribute("restaurants", user.getRestaurantOwner().getRestaurants());
        return "owner";
    }
}
