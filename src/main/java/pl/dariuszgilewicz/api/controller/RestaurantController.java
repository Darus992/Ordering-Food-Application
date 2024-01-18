package pl.dariuszgilewicz.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.dariuszgilewicz.business.FoodMenuService;
import pl.dariuszgilewicz.business.RestaurantService;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
public class RestaurantController {


    private FoodMenuService foodMenuService;
    private RestaurantService restaurantService;
    private UserService userService;


    @GetMapping("/restaurant/create")
    public String showRestaurantForm(Model model) {
        model.addAttribute("restaurantRequestForm", new RestaurantRequestForm());
        return "restaurant_form";
    }

    @GetMapping(value = "/restaurant/{restaurantEmail}")
    public String showCurrentRestaurantDetails(
            @PathVariable String restaurantEmail,
            Authentication authentication,
            Model model
    ) {
        Restaurant restaurant = restaurantService.findRestaurantByEmail(restaurantEmail);
        boolean isAuthenticated = UserService.checkIfIsAuthenticated(model, authentication);
        UserService.assignRoleDependsOnAuthentication(authentication, model, isAuthenticated);
        model.addAttribute("restaurantEmail", restaurantEmail);
        model.addAttribute("restaurant", restaurant);
        return "restaurant_details";
    }

    @GetMapping(value = "/restaurants")
    public String findRestaurantsBySearchTerm(
            @RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm,
            Authentication authentication,
            Model model
    ) {
        List<Restaurant> restaurants = restaurantService.findRestaurantsNearYouByAddress(searchTerm);
        boolean isAuthenticated = UserService.checkIfIsAuthenticated(model, authentication);
        UserService.assignRoleDependsOnAuthentication(authentication, model, isAuthenticated);
        model.addAttribute("restaurants", restaurants);


        List<String> categories = Arrays.asList("Pizza", "Burgers");
        model.addAttribute("categories", categories);
        return "restaurants_lists";
    }

    @GetMapping(value = "/restaurants/category")
    public String showRestaurantsWithPickedCategory(
            @RequestParam(name = "foodCategory", required = false) String foodCategory,
            Authentication authentication,
            Model model
    ) {
        model.addAttribute("selectedCategory", foodCategory);
        List<Restaurant> restaurants = restaurantService.findAllRestaurantsWithPickedCategory(foodCategory);
        boolean isAuthenticated = UserService.checkIfIsAuthenticated(model, authentication);
        UserService.assignRoleDependsOnAuthentication(authentication, model, isAuthenticated);
        model.addAttribute("restaurants", restaurants);

        List<String> categories = Arrays.asList("Pizza", "Burgers");
        model.addAttribute("categories", categories);
        return "restaurants_lists";
    }

    @GetMapping("/restaurant/create-food-menu/{restaurantEmail}")
    public String showFoodMenuForm(@PathVariable String restaurantEmail, Model model) {
        model.addAttribute("foodMenuForm", new FoodMenu());
        model.addAttribute("restaurantEmail", restaurantEmail);
        return "food_menu_form";
    }

    @GetMapping("/restaurant/create-food/{restaurantEmail}")
    public String showFoodForm(@PathVariable String restaurantEmail, Model model) {
        model.addAttribute("foodForm", new Food());
        model.addAttribute("restaurantEmail", restaurantEmail);
        return "food_form";
    }

    @PostMapping("/restaurant/create")
    public String createRestaurantForm(@ModelAttribute("restaurantRequestForm") RestaurantRequestForm restaurantRequestForm) {
        String userName = userService.getCurrentUserName();
        restaurantService.createRestaurantAndAssignToOwner(restaurantRequestForm, userName);
        return "redirect:/owner";
    }

    @PostMapping("/restaurant/create-food-menu/{restaurantEmail}")
    public String createFoodMenuForm(
            @ModelAttribute("foodMenuForm") FoodMenu foodMenu,
            @RequestParam String restaurantEmail,
            Model model
    ) {
        model.addAttribute("restaurantEmail", restaurantEmail);
        foodMenuService.createFoodMenuAndAssignToRestaurant(foodMenu, restaurantEmail);
        return "redirect:/owner";
    }

    @PostMapping("/restaurant/create-food/{restaurantEmail}")
    public String createFoodForm(
            @ModelAttribute("foodForm") Food food,
            @RequestParam String restaurantEmail,
            Model model
    ) {
        model.addAttribute("restaurantEmail", restaurantEmail);
        foodMenuService.createFoodAndAssignToFoodMenu(food, restaurantEmail);
        return "redirect:/owner";
    }
}
