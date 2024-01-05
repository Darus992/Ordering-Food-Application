package pl.dariuszgilewicz.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.dariuszgilewicz.business.RestaurantService;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;

import java.util.List;


@Controller
@AllArgsConstructor
public class HomeController {

    private RestaurantService restaurantService;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("isAuthenticated", authentication != null && authentication.isAuthenticated());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/restaurants")
    public String findRestaurantsByName(
            @RequestParam(name = "restaurantName", required = false, defaultValue = "") String restaurantName,
            Authentication authentication,
            Model model
    ){
        List<Restaurant> foundRestaurants = restaurantService.findRestaurantsByName(restaurantName);
        model.addAttribute("restaurants", foundRestaurants);
        model.addAttribute("isAuthenticated", authentication != null && authentication.isAuthenticated());
        return "restaurants_lists";
    }

    @GetMapping(value = "/restaurants/{restaurantEmail}")
    public String showCurrentRestaurantDetails(@PathVariable String restaurantEmail, Authentication authentication, Model model){
        Restaurant restaurant = restaurantService.findRestaurantByEmail(restaurantEmail);
        model.addAttribute("restaurantEmail", restaurantEmail);
        model.addAttribute("isAuthenticated", authentication != null && authentication.isAuthenticated());
        model.addAttribute("restaurant", restaurant);
        return "restaurant_details";
    }
}
