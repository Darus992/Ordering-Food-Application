package pl.dariuszgilewicz.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.dariuszgilewicz.business.RestaurantOwnerService;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

@Controller
@AllArgsConstructor
public class RestaurantOwnerController {

    private RestaurantOwnerService restaurantOwnerService;
    private UserService userService;

    @GetMapping(value = "/owner")
    public String showOwnerPage(Model model) {
        String userName = userService.getCurrentUserName();
        User user = userService.findUserByUserName(userName);
        model.addAttribute("restaurants", user.getRestaurants());
        return "restaurant";
    }

    @GetMapping(value = "/owner/create")
    public String showOwnerForm(Model model) {
        model.addAttribute("ownerForm", new RestaurantOwner());
        return "restaurant_owner_form";
    }

    @PostMapping(value = "/owner/create")
    public String createRestaurantOwner(@ModelAttribute("ownerForm") RestaurantOwner restaurantOwner) {
        String userName = userService.getCurrentUserName();
        restaurantOwnerService.createRestaurantOwner(userName, restaurantOwner);
        return "redirect:/";
    }
}
