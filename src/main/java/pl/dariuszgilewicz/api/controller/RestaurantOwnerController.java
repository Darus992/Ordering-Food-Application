package pl.dariuszgilewicz.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.RestaurantRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;
import pl.dariuszgilewicz.infrastructure.util.FileUploadUtil;

@Controller
@AllArgsConstructor
public class RestaurantOwnerController {
    private UserService userService;
    private RestaurantRepository restaurantRepository;
    private RestaurantJpaRepository restaurantJpaRepository;

    @GetMapping("/owner")
    public String showOwnerPage(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("restaurants", user.getRestaurantOwner().getRestaurants());
        return "owner";
    }

    @PostMapping("/update-menu-image")
    public String updateMenuImage(
            @RequestParam("menuImage") MultipartFile menuImage,
            @RequestParam("restaurantEmail") String restaurantEmail
            ) {
        RestaurantEntity restaurant = restaurantRepository.findRestaurantEntityByEmail(restaurantEmail);
        byte[] convertFileToBytes = FileUploadUtil.convertFileToBytes(menuImage);
        restaurant.getFoodMenu().setFoodMenuImage(convertFileToBytes);
        restaurantJpaRepository.save(restaurant);
        return "redirect:/owner";
    }
}
