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
import org.springframework.web.multipart.MultipartFile;
import pl.dariuszgilewicz.business.FoodMenuService;
import pl.dariuszgilewicz.business.OrderService;
import pl.dariuszgilewicz.business.RestaurantService;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RestaurantOwnerController {
    private UserService userService;
    private RestaurantService restaurantService;
    private RestaurantJpaRepository restaurantJpaRepository;
    private FoodMenuService foodMenuService;
    private RestaurantEntityMapper restaurantEntityMapper;
    private OrderService orderService;
    private OrderRepository orderRepository;


    @GetMapping("/owner")
    public String showOwnerPage(Model model) {
        User user = getCurrentUser(model);
        model.addAttribute("restaurants", user.getOwner().getRestaurants());
        return "owner";
    }

    @GetMapping("/owner/update-profile")
    public String showOwnerUpdatePage(Model model) {
        User user = getCurrentUser(model);
        model.addAttribute("userForm", new User());
        model.addAttribute("user", user);
        return "owner_update_data_form";
    }

    @GetMapping("/owner/restaurant/{restaurantEmail}/details")
    public String showRestaurantDetailsForEditePage(@PathVariable String restaurantEmail, Model model) {
        addAttributeToModelFromCurrentUser(restaurantEmail, model);
        model.addAttribute("defaultTab", "list-details-list");
        return "owner_restaurant_details_edit";
    }

    @GetMapping("/owner/restaurant/{restaurantEmail}/create-food")
    public String showRestaurantCreateFoodPage(@PathVariable String restaurantEmail, Model model) {
        addAttributeToModelFromCurrentUser(restaurantEmail, model);
        model.addAttribute("defaultTab", "list-food-form-list");
        return "owner_restaurant_details_edit";
    }

    @GetMapping("/owner/restaurant/{restaurantEmail}/delete-food-from-menu")
    public String showRestaurantDeleteFoodFromMenuPage(@PathVariable String restaurantEmail, Model model) {
        addAttributeToModelFromCurrentUser(restaurantEmail, model);
        model.addAttribute("defaultTab", "list-food-menu-list");
        return "owner_restaurant_details_edit";
    }

    @GetMapping("/owner/restaurant/{restaurantEmail}/edite-food-from-menu")
    public String showRestaurantEditFoodFromMenuPage(@PathVariable String restaurantEmail, Model model) {
        addAttributeToModelFromCurrentUser(restaurantEmail, model);
        model.addAttribute("defaultTab", "list-food-menu-list");
        return "owner_restaurant_details_edit";
    }

    @GetMapping("/owner/restaurant/{restaurantEmail}/update-restaurant-image")
    public String showRestaurantUpdateImagePage(@PathVariable String restaurantEmail, Model model) {
        addAttributeToModelFromCurrentUser(restaurantEmail, model);
        model.addAttribute("defaultTab", "list-restaurant-image-list");
        return "owner_restaurant_details_edit";
    }

    @GetMapping("/owner/restaurant/{restaurantEmail}/update-schedule")
    public String showUpdateSchedulePage(@PathVariable String restaurantEmail, Model model) {
        addAttributeToModelFromCurrentUser(restaurantEmail, model);
        model.addAttribute("defaultTab", "list-work-schedule-list");
        return "owner_restaurant_details_edit";
    }

    @GetMapping("/owner/restaurant/{restaurantEmail}/order-update")
    public String showRestaurantOrdersPage(@PathVariable String restaurantEmail, Model model) {
        addAttributeToModelFromCurrentUser(restaurantEmail, model);
        model.addAttribute("defaultTab", "list-orders-list");
        return "owner_restaurant_details_edit";
    }

    @GetMapping("/owner/restaurant/{restaurantEmail}/order-details/{orderNumber}")
    public String showRestaurantOrderDetailsPage(
            @PathVariable String restaurantEmail,
            @PathVariable Integer orderNumber,
            Model model
    ) {
        addAttributeToModelFromCurrentUser(restaurantEmail, model);
        Orders order = orderRepository.findOrderByOrderNumber(orderNumber);
        model.addAttribute("order", order);
        return "order_details";
    }

    @PatchMapping("/owner/restaurant/{restaurantEmail}/order-update")
    public String updateRestaurantOrder(
            @PathVariable String restaurantEmail,
            @RequestParam String statusChanger,
            @RequestParam Integer orderNumber,
            Model model
    ) {
        addAttributeToModelFromCurrentUser(restaurantEmail, model);
        orderService.updateOrderStatus(orderNumber, statusChanger);
        return "redirect:/owner";
    }

    @PatchMapping("/owner/update-profile")
    public String updateOwnerProfile(
            @Valid @ModelAttribute("userForm") User userForm,
            @RequestParam(name = "emailParam") String userEmail,
            BindingResult bindingResult,
            Model model
    ) {
        if (checkIfHasErrors(userForm, bindingResult, model)) {
            return "owner_update_data_form";
        }

        userService.updateUserData(userForm, userEmail);
        return "redirect:/owner";
    }

    @PatchMapping("/owner/restaurant/{restaurantEmail}/details")
    public String updateRestaurantDetails(
            @PathVariable String restaurantEmail,
            @ModelAttribute Restaurant restaurant
    ) {
        RestaurantEntity restaurantEntity = getRestaurantEntityByEmail(restaurantEmail);
        restaurantService.updateRestaurantDetails(restaurantEntity, restaurant);
        return "redirect:/owner";
    }

    @PatchMapping("/owner/restaurant/{restaurantEmail}/delete-food-from-menu")
    public String deleteFoodFromMenu(
            @PathVariable String restaurantEmail,
            @RequestParam("foodId") Integer foodId
    ) {
        foodMenuService.deleteFoodFromMenu(foodId, restaurantEmail);
        return "redirect:/owner";
    }

    @DeleteMapping("/owner/delete-account")
    public String deleteAccount(@RequestParam("userEmail") String userEmail) {
        userService.deleteAccount(userEmail);
        return "redirect:/logout";
    }

    @PatchMapping("/owner/restaurant/{restaurantEmail}/edite-food-from-menu")
    public String editFoodFromMenu(
            @PathVariable String restaurantEmail,
            @Valid @ModelAttribute Food food,
            BindingResult bindingResult,
            Model model
    ) {
        RestaurantEntity restaurantEntity = getRestaurantEntityByEmail(restaurantEmail);

        if (checkIfHasErrors(food, bindingResult, model, restaurantEntity)) {
            return "owner_restaurant_details_edit";
        }

        foodMenuService.editFoodFromMenu(food);
        return "redirect:/owner";
    }

    @PatchMapping("/owner/restaurant/{restaurantEmail}/update-restaurant-image")
    public String updateRestaurantImage(
            @PathVariable String restaurantEmail,
            @RequestParam(name = "restaurantImage") MultipartFile restaurantImage,
            @RequestParam(name = "image", required = false) String param
    ) {
        if (!restaurantImage.isEmpty()) {
            RestaurantEntity restaurantEntity = getRestaurantEntityByEmail(restaurantEmail);
            restaurantService.updateRestaurantImage(restaurantImage, restaurantEntity, param);
        }
        return "redirect:/owner";
    }

    @PatchMapping("/owner/restaurant/{restaurantEmail}/update-schedule")
    public String updateRestaurantSchedule(
            @PathVariable String restaurantEmail,
            @ModelAttribute Restaurant restaurant
    ) {
        RestaurantEntity restaurantEntity = getRestaurantEntityByEmail(restaurantEmail);
        restaurantService.updateRestaurantSchedule(restaurantEntity, restaurant);
        return "redirect:/owner";
    }

    @PostMapping("/owner/restaurant/{restaurantEmail}/create-food")
    public String createFood(
            @PathVariable String restaurantEmail,
            @Valid @ModelAttribute("food") Food food,
            BindingResult bindingResult,
            Model model
    ) {
        RestaurantEntity restaurantEntity = getRestaurantEntityByEmail(restaurantEmail);

        if (checkIfHasErrors(food, bindingResult, model, restaurantEntity)) {
            return "owner_restaurant_details_edit";
        }

        foodMenuService.assignFoodToFoodMenu(food, restaurantEntity);
        return "redirect:/owner";
    }

    private boolean checkIfHasErrors(Food food, BindingResult bindingResult, Model model, RestaurantEntity restaurantEntity) {
        if (food.getFileImageToUpload().isEmpty()) {
            bindingResult.addError(new FieldError("food", "fileImageToUpload", "The image file is required."));
        }

        if (bindingResult.hasErrors()) {
            Restaurant restaurant = restaurantEntityMapper.mapFromEntity(restaurantEntity);
            model.addAttribute("restaurant", restaurant);
            model.addAttribute("defaultTab", "list-food-form-list");
            return true;
        }
        return false;
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

    private void addAttributeToModelFromCurrentUser(String restaurantEmail, Model model) {
        User user = getCurrentUser(model);

        List<Restaurant> restaurants = user.getOwner().getRestaurants();
        for (Restaurant restaurant : restaurants) {
            List<Orders> result = restaurantService.createOrdersListByOrderNumber(restaurant.getCustomerOrdersNumbers());
            restaurant.setOrders(result);

            if (restaurant.getRestaurantEmail().equals(restaurantEmail)) {
                model.addAttribute("restaurant", restaurant);
                break;
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("food", new Food());
    }

    private User getCurrentUser(Model model) {
        Optional<User> optional = userService.getCurrentOptionalUser(model);
        return optional.orElseThrow(
                () -> new EntityNotFoundException("User: [%s] not found.".formatted(optional))
        );
    }

    private RestaurantEntity getRestaurantEntityByEmail(String restaurantEmail) {
        Optional<RestaurantEntity> optional = restaurantJpaRepository.findByEmail(restaurantEmail);

        return optional.orElseThrow(
                () -> new EntityNotFoundException("RestaurantEntity with email: [%s] not found.".formatted(restaurantEmail))
        );
    }

    private static void validate(BindingResult bindingResult, User userForm) {
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "username", "required.username", "Username is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "email", "required.email", "User email is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "owner.name", "required.name", "Name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "owner.surname", "required.surname", "Surname is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "owner.pesel", "required.pesel", "Pesel is required.");

        if (userForm.getOwner().getPesel() != null) {
            validatePesel(userForm.getOwner().getPesel(), bindingResult);
        }

        if (userForm.getUsername().length() < 5) {
            bindingResult.addError(new FieldError("userForm", "username", "Username must contain at least 5 characters."));
        }

        if (!userForm.getPassword().isEmpty() && userForm.getPassword().length() < 5) {
            bindingResult.addError(new FieldError("userForm", "password", "Password must contain at least 5 characters."));
        }
    }

    private static void validatePesel(String pesel, BindingResult bindingResult) {
        if (pesel.length() != 11) {
            bindingResult.addError(new FieldError("userForm", "owner.pesel", "Pesel number should have 11 numbers."));
        }
        if (!pesel.matches("^\\d+$")) {
            bindingResult.addError(new FieldError("userForm", "owner.pesel", "Pesel must contain only digits."));
        }
    }
}
