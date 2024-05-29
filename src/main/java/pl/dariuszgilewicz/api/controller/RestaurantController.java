package pl.dariuszgilewicz.api.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.dariuszgilewicz.business.RestaurantService;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RestaurantController {

    private RestaurantService restaurantService;
    private UserService userService;


    @GetMapping("/restaurant/create")
    public String showRestaurantForm(Model model) {
        model.addAttribute("restaurantRequestForm", new RestaurantRequestForm());
        return "restaurant_form";
    }

    @GetMapping(value = "/restaurants/{restaurantEmail}")
    public String showCurrentRestaurantDetails(
            @PathVariable String restaurantEmail,
            Model model
    ) {
        findAndAddUserToModelAttributeIfIsAuthenticated(model);
        Restaurant restaurant = restaurantService.findRestaurantByEmail(restaurantEmail);

        model.addAttribute("restaurant", restaurant);
        return "restaurant_details";
    }

    @GetMapping(value = "/restaurants")
    public String findRestaurantsBySearchTerm(
            @RequestParam(name = "searchTerm", required = false, defaultValue = "Warszawa") String searchTerm,
            Model model
    ) {
        findAndAddUserToModelAttributeIfIsAuthenticated(model);
        List<Restaurant> restaurants = restaurantService.findRestaurantsBySearchTerm(searchTerm);
        model.addAttribute("restaurants", restaurants);
        return "restaurants_lists";
    }

    //  TODO:  DO ZAIMPLEMENTOWANIA
//    @GetMapping(value = "/restaurants/category")
//    public String showRestaurantsWithPickedCategory(
//            @RequestParam(name = "foodCategory", required = false) String foodCategory,
//            Authentication authentication,
//            Model model
//    ) {
//        model.addAttribute("selectedCategory", foodCategory);
//        List<Restaurant> restaurants = restaurantService.findAllRestaurantsWithSelectedCategory(foodCategory);
//        boolean isAuthenticated = userService.checkIfIsAuthenticated(model, authentication);
//        userService.assignRoleDependsOnAuthentication(authentication, model, isAuthenticated);
//        model.addAttribute("restaurants", restaurants);
//
//        List<String> categories = Arrays.asList("Pizza", "Burgers");
//        model.addAttribute("categories", categories);
//        return "restaurants_lists";
//    }

    @PostMapping("/restaurant/create")
    public String createRestaurantForm(
            @ModelAttribute("restaurantRequestForm") RestaurantRequestForm restaurantRequestForm,
            Model model
    ) {
        Optional<User> optionalUser = userService.getCurrentOptionalUser(model);
        User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User Entity: [%s] not found.".formatted(optionalUser)));
        restaurantService.createRestaurantAndAssignToOwner(restaurantRequestForm, user.getOwner());
        return "redirect:/owner";
    }

    private void findAndAddUserToModelAttributeIfIsAuthenticated(Model model) {
        Optional<User> optionalUser = userService.getCurrentOptionalUser(model);
        optionalUser.ifPresent(user -> model.addAttribute("user", user));
    }
}