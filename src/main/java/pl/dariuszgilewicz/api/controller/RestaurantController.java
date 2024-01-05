package pl.dariuszgilewicz.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.dariuszgilewicz.business.FoodMenuService;
import pl.dariuszgilewicz.business.RestaurantService;
import pl.dariuszgilewicz.business.ScheduleService;
import pl.dariuszgilewicz.infrastructure.model.*;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.ArrayList;

@Controller
@AllArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {

    private static final String CREATE_RESTAURANT = "/create";
    private static final String CREATE_FOOD_MENU = "/create-food-menu/{restaurantEmail}";

    private RestaurantService restaurantService;
    private FoodMenuService foodMenuService;
    private ScheduleService scheduleService;
    private UserService userService;


    @GetMapping(CREATE_RESTAURANT)
    public String showRestaurantForm(Model model) {
        model.addAttribute("restaurantForm", new Restaurant());
        return "restaurant_form";
    }

    @GetMapping(CREATE_FOOD_MENU)
    public String showFoodMenuForm(@PathVariable String restaurantEmail, Model model) {
        model.addAttribute("foodMenuForm", new FoodMenu());
        model.addAttribute("restaurantEmail", restaurantEmail);
        return "food_menu_form";
    }

    @GetMapping("/create-food/{restaurantEmail}")
    public String showFoodForm(@PathVariable String restaurantEmail, Model model) {
        model.addAttribute("foodForm", new Food());
        model.addAttribute("restaurantEmail", restaurantEmail);
        return "food_form";
    }

    @GetMapping("/create-schedule/{restaurantEmail}")
    public String showScheduleForm(@PathVariable String restaurantEmail, Model model){

        Schedule schedule = new Schedule(new ArrayList<>());
        for (int i = 1; i <= 7; i++){
            schedule.addRestaurantOpeningTime(new RestaurantOpeningTime());
        }

        model.addAttribute("scheduleForm", schedule);
        model.addAttribute("restaurantEmail", restaurantEmail);
        return "schedule_form";
    }

    @PostMapping(CREATE_RESTAURANT)
    public String createRestaurant(@ModelAttribute("restaurantForm") Restaurant restaurant) {
        String userName = userService.getCurrentUserName();
        restaurantService.createRestaurant(userName, restaurant);
        return "redirect:/owner";
    }

    @PostMapping(CREATE_FOOD_MENU)
    public String createFoodMenu(
            @ModelAttribute("foodMenuForm") FoodMenu foodMenu,
            @RequestParam String restaurantEmail,
            Model model
    ) {
        model.addAttribute("restaurantEmail", restaurantEmail);
        foodMenuService.createAndAddFoodMenu(foodMenu, restaurantEmail);
        return "redirect:/owner";
    }

    @PostMapping("/create-food/{restaurantEmail}")
    public String createFood(
            @ModelAttribute("foodForm") Food food,
            @RequestParam String restaurantEmail,
            Model model
    ) {
        model.addAttribute("restaurantEmail", restaurantEmail);
        foodMenuService.createFoodAndAddToMenu(food, restaurantEmail);
        return "redirect:/owner";
    }

    @PostMapping("/create-schedule/{restaurantEmail}")
    public String createSchedule(
            @ModelAttribute("scheduleForm") Schedule schedule,
            @RequestParam String restaurantEmail,
            Model model
    ){
        model.addAttribute("restaurantEmail", restaurantEmail);
        scheduleService.createScheduleAndAddToRestaurant(schedule, restaurantEmail);
        return "redirect:/owner";
    }
}
