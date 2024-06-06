package pl.dariuszgilewicz.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.dariuszgilewicz.api.dto.OrderDTO;
import pl.dariuszgilewicz.api.dto.RestaurantDTO;
import pl.dariuszgilewicz.api.dto.mapper.BusinessUserMapper;
import pl.dariuszgilewicz.api.dto.mapper.FoodMapper;
import pl.dariuszgilewicz.api.dto.mapper.OrderMapper;
import pl.dariuszgilewicz.api.dto.mapper.RestaurantMapper;
import pl.dariuszgilewicz.api.dto.request.FoodRequestFormDTO;
import pl.dariuszgilewicz.api.dto.request.RestaurantImageRequestDTO;
import pl.dariuszgilewicz.business.FoodMenuService;
import pl.dariuszgilewicz.business.OrderService;
import pl.dariuszgilewicz.business.RestaurantService;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.enums.AvailableOrderStatuses;
import pl.dariuszgilewicz.infrastructure.database.enums.RestaurantImageType;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.model.Food;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.exception.NoContentException;
import pl.dariuszgilewicz.infrastructure.model.exception.ResourceNotFoundException;
import pl.dariuszgilewicz.infrastructure.model.exception.UnauthorizedException;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/owner")
@Tag(name = "Owner", description = "Endpoints for managing owner details")
public class RestaurantOwnerRestController {

    public final String BASE_URL = "http://localhost:8190/ordering-food-application";

    private final UserService userService;
    private final RestaurantMapper restaurantMapper;
    private final OrderMapper orderMapper;
    private final BusinessUserMapper businessUserMapper;
    private final FoodMapper foodMapper;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantService restaurantService;
    private final FoodMenuService foodMenuService;


    @Operation(
            summary = "Show all restaurants owned by the authenticated owner",
            description = "Retrieves a list of all restaurants owned by the authenticated owner."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = RestaurantDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping
    public ResponseEntity<?> showAllOwnerRestaurants() {
        User user = getCurrentAuthenticatedBusinessUser();
        List<RestaurantDTO> restaurantDTOList = restaurantMapper.mapToDTOList(user.getOwner().getRestaurants(), BASE_URL);
        return ResponseEntity.ok(restaurantDTOList);
    }


    @Operation(
            summary = "Show order details for a specific restaurant",
            description = "Retrieves the details of a specific order for a restaurant."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @GetMapping("/restaurant/{restaurantEmail}/order-details/{orderNumber}")
    public ResponseEntity<?> showRestaurantOrderDetails(
            @Parameter(description = "Email of the restaurant", required = true, example = "example@restaurant.com")
            @PathVariable String restaurantEmail,
            @Parameter(description = "Order number", required = true, example = "12345")
            @PathVariable String orderNumber
    ) {
        User user = getCurrentAuthenticatedBusinessUser();

        Optional<OrderDTO> orderDTO = user.getOwner().getRestaurants().stream()
                .filter(restaurant -> restaurant.getRestaurantEmail().equals(restaurantEmail))
                .flatMap(restaurant -> restaurant.getCustomerOrdersNumbers().stream())
                .filter(orderNum -> orderNum.equals(Integer.parseInt(orderNumber)))
                .map(orderRepository::findOrderByOrderNumber)
                .map(orderMapper::mapToDTO)
                .findFirst();

        return orderDTO.map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order with orderNumber: [%s] not found in restaurant with email: [%s]"
                                .formatted(orderNumber, restaurantEmail)));
    }


    @Operation(
            summary = "Update order status for a specific restaurant",
            description = "Updates the status of a specific order for a restaurant."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "204", description = "No content available", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content)
    })
    @PatchMapping("/restaurant/{restaurantEmail}/order-update")
    public ResponseEntity<?> updateOrderStatus(
            @Parameter(description = "Email of the restaurant", required = true, example = "example@restaurant.com")
            @PathVariable String restaurantEmail,
            @Parameter(description = "New order status", required = true, schema = @Schema(implementation = AvailableOrderStatuses.class))
            @RequestParam AvailableOrderStatuses availableOrderStatuses,
            @Parameter(description = "Order number", required = true, example = "12345")
            @RequestParam String orderNumber
    ) {
        User user = getCurrentAuthenticatedBusinessUser();

        if (checkIfCurrentBusinessUserHaveRestaurantWithThisEmail(restaurantEmail, user.getOwner().getRestaurants())) {
            orderService.updateOrderStatus(Integer.parseInt(orderNumber), availableOrderStatuses.name());
            return ResponseEntity.ok("Order status changed successfully");
        }
        throw new NoContentException("User with email: [%s] does not have a restaurant with email: [%s]"
                .formatted(user.getEmail(), restaurantEmail));
    }


    @Operation(
            summary = "Update owner profile",
            description = "Updates the profile details of the authenticated owner. The password can be optionally updated.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    mediaType = "multipart/form-data"))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "204", description = "No content available", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content)
    })
    @PatchMapping("/update-profile")
    public ResponseEntity<?> updateOwnerProfile(
            @Parameter(description = "Update username by filling in this field or leave it empty to keep the current.", example = "john_doe")
            @Size(min = 5, message = "Username must contain at least 5 characters.")
            @RequestParam(required = false)
            String username,
            @Parameter(description = "Update user email by filling in this field or leave it empty to keep the current.", example = "john.doe@example.com")
            @Valid @Pattern(regexp = "^(.+)@(.+)$", message = "Email must contains @ sign")
            @RequestParam(required = false)
            String email,
            @Parameter(description = "Update the name by filling in this field or leave it empty to keep the current.", example = "John")
            @RequestParam(required = false)
            String ownerName,
            @Parameter(description = "Update the surname by filling in this field or leave it empty to keep the current.", example = "Doe")
            @RequestParam(required = false)
            String ownerSurname,
            @Parameter(description = "New password for the owner. Leave blank to keep the current password.")
            @Size(message = "Password must contain at least 5 characters.", min = 5)
            @RequestParam(required = false)
            String password,
            @Parameter(description = "Update pesel by filling in this field or leave it empty to keep the current.", example = "91522584927")
            @Size(message = "Pesel number should have 11 numbers.", min = 11, max = 11)
            @Pattern(regexp = "^\\d+$", message = "Pesel must contain only digits.")
            @RequestParam(required = false)
            String ownerPesel
    ) {
        User currentUser = getCurrentAuthenticatedBusinessUser();

        User userForm = businessUserMapper.mapArgumentToUser(username, email, ownerName, ownerSurname, password, ownerPesel, currentUser);
        userService.updateUserData(userForm, currentUser.getEmail());
        return ResponseEntity.ok("Owner profile updated successfully");
    }


    @Operation(
            summary = "Update restaurant details",
            description = "Endpoint for updating restaurant details.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    mediaType = "multipart/form-data"))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant details updated successfully", content = @Content),
            @ApiResponse(responseCode = "204", description = "No content found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content)
    })
    @PatchMapping("/restaurant/{restaurantEmail}/details")
    public ResponseEntity<?> updateRestaurantDetails(
            @PathVariable
            @Parameter(description = "Email of the restaurant to update", required = true, example = "example@restaurant.com")
            String restaurantEmail,
            @RequestParam(required = false)
            @Parameter(description = "Update the restaurant name by filling in this field or leave it empty to keep the current name.")
            @Valid @Pattern(regexp = "\\D*", message = "The restaurant name must not contain any digits.")
            String restaurantName,
            @RequestParam(required = false)
            @Parameter(description = "Update phone number by filling in this field or leave it empty to keep the current phone number.")
            @Valid @Pattern(regexp = "^\\d+$", message = "Phone number must contain only digits.")
            @Size(message = "The phone number size cannot be larger or smaller than 9.", min = 9, max = 9)
            String restaurantPhone,
            @RequestParam(required = false)
            @Parameter(description = "Update the restaurant email by filling in this field or leave it empty to keep the current email.")
            @Valid @Pattern(regexp = "^(.+)@(.+)$", message = "Email must contains @ sign")
            String restaurantEmailToUpdate,
            @RequestParam(required = false)
            @Parameter(description = "Update city by filling in this field or leave it empty to keep the current city.")
            @Valid @Pattern(regexp = "\\D*", message = "City must not contain any digits.")
            String restaurantAddressCity,
            @RequestParam(required = false)
            @Parameter(description = "Update district by filling in this field or leave it empty to keep the current district.")
            @Valid @Pattern(regexp = "\\D*", message = "District must not contain any digits.")
            String restaurantAddressDistrict,
            @RequestParam(required = false)
            @Parameter(description = "Update postal code by filling in this field or leave it empty to keep the current postal code.")
            String restaurantAddressPostalCode,
            @RequestParam(required = false)
            @Parameter(description = "Update address street by filling in this field or leave it empty to keep the current address street.")
            String restaurantAddressStreet
    ) {
        User user = getCurrentAuthenticatedBusinessUser();

        if (checkIfCurrentBusinessUserHaveRestaurantWithThisEmail(restaurantEmail, user.getOwner().getRestaurants())) {
            RestaurantEntity currentRestaurant = getRestaurantByEmail(restaurantEmail);
            Restaurant updatedRestaurant = restaurantMapper.mapRestaurantDetailsFormToUpdateFromDTO(
                    restaurantName,
                    restaurantPhone,
                    restaurantEmailToUpdate,
                    restaurantAddressCity,
                    restaurantAddressDistrict,
                    restaurantAddressPostalCode,
                    restaurantAddressStreet,
                    currentRestaurant
            );
            restaurantService.updateRestaurantDetails(currentRestaurant, updatedRestaurant);
            return ResponseEntity.ok("Restaurant details updated successfully.");
        }
        throw new NoContentException("User with email: [%s] does not have a restaurant with email: [%s]"
                .formatted(user.getEmail(), restaurantEmail));
    }


    @Operation(
            summary = "Create new food.",
            description = "Create new food, or find if already exists and assign to restaurant menu",
            requestBody = @RequestBody(content = @Content(mediaType = "multipart/form-data",
                    schema = @Schema(implementation = FoodRequestFormDTO.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "204", description = "No content available", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Page not found", content = @Content)
    })
    @PostMapping("/restaurant/{restaurantEmail}/create-food")
    public ResponseEntity<?> createFoodAndAssignToFoodMenu(
            @Parameter(description = "Email of the restaurant for which we create food.", required = true, example = "example@restaurant.com")
            @Valid @PathVariable String restaurantEmail,
            @Parameter(description = "Food form details", required = true)
            @Valid @RequestBody FoodRequestFormDTO requestFormDTO
    ) {
        User user = getCurrentAuthenticatedBusinessUser();

        if (checkIfCurrentBusinessUserHaveRestaurantWithThisEmail(restaurantEmail, user.getOwner().getRestaurants())) {
            RestaurantEntity restaurantEntity = getRestaurantByEmail(restaurantEmail);
            Food food = foodMapper.mapFromFoodRequestFormDTO(requestFormDTO);

            foodMenuService.assignFoodToFoodMenu(food, restaurantEntity);
            return ResponseEntity.ok("Food created and add to food menu successfully");
        } else {
            throw new ResourceNotFoundException("Page not found. Ensure the provided restaurant email: [%s] is correct."
                    .formatted(restaurantEmail));
        }
    }


    @Operation(
            summary = "Update restaurant schedule",
            description = "Updates the schedule for the specified restaurant."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "204", description = "No content available", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Page not found", content = @Content)
    })
    @PatchMapping("/restaurant/{restaurantEmail}/update-schedule")
    public ResponseEntity<?> updateRestaurantSchedule(
            @Parameter(description = "Email of the restaurant for which we update schedule.", required = true, example = "example@restaurant.com")
            @Valid @PathVariable String restaurantEmail,
            @Parameter(description = "Opening hour for the specified day.", example = "08:00")
            @Valid @RequestParam(required = false) String openingHour,
            @Parameter(description = "Closing hour for the specified day.", example = "18:00")
            @Valid @RequestParam(required = false) String closeHour,
            @Parameter(description = "Starting day of the week for the updated schedule.", example = "MONDAY")
            @Valid @RequestParam DayOfWeek dayOfWeekFrom,
            @Parameter(description = "Ending day of the week for the updated schedule.", example = "FRIDAY")
            @Valid @RequestParam DayOfWeek dayOfWeekTill
    ) {
        User user = getCurrentAuthenticatedBusinessUser();

        if (checkIfCurrentBusinessUserHaveRestaurantWithThisEmail(restaurantEmail, user.getOwner().getRestaurants())) {
            RestaurantEntity restaurantEntity = getRestaurantByEmail(restaurantEmail);
            Restaurant restaurantWithScheduleToUpdate = restaurantMapper.mapScheduleDataToRestaurant(openingHour, closeHour, dayOfWeekFrom, dayOfWeekTill, restaurantEntity);

            restaurantService.updateRestaurantSchedule(restaurantEntity, restaurantWithScheduleToUpdate);
            return ResponseEntity.ok("Restaurant schedule updated successfully");
        } else {
            throw new ResourceNotFoundException("Page not found. Ensure the provided restaurant email: [%s] is correct."
                    .formatted(restaurantEmail));
        }
    }

    @Operation(
            summary = "Update restaurant image",
            description = "Update the restaurant image by image type (CARD, HEADER)",
            requestBody = @RequestBody(content = @Content(mediaType = "multipart/form-data",
                    schema = @Schema(implementation = RestaurantImageRequestDTO.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "204", description = "No content available", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Page not found", content = @Content)
    })
    @PatchMapping("/restaurant/{restaurantEmail}/update-restaurant-image")
    public ResponseEntity<?> updateRestaurantImage(
            @Parameter(description = "Email of the restaurant for which we update image", required = true, example = "example@restaurant.com")
            @PathVariable String restaurantEmail,
            @Parameter(description = "New restaurant image file")
            @Valid @RequestBody(required = true)
            MultipartFile requestImage,
            @Parameter(description = "Restaurant image type (CARD, HEADER)")
            @Valid @RequestParam
            RestaurantImageType restaurantImageType
    ) {
        User user = getCurrentAuthenticatedBusinessUser();

        if (checkIfCurrentBusinessUserHaveRestaurantWithThisEmail(restaurantEmail, user.getOwner().getRestaurants())) {
            RestaurantEntity restaurantEntity = getRestaurantByEmail(restaurantEmail);
            restaurantService.updateRestaurantImage(requestImage, restaurantEntity, restaurantImageType.name());
            return ResponseEntity.ok("Restaurant image: [%s] updated successfully".formatted(restaurantImageType.name()));
        } else {
            throw new ResourceNotFoundException("Page not found. Ensure the provided restaurant email: [%s] is correct."
                    .formatted(restaurantEmail));
        }
    }


    @Operation(
            summary = "Delete food from restaurant menu",
            description = "Delete food from your restaurant menu but not from the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "204", description = "No content available", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Page not found", content = @Content)
    })
    @PatchMapping("/restaurant/{restaurantEmail}/delete-food-from-menu")
    public ResponseEntity<?> deleteFoodFromMenu(
            @Parameter(description = "Email of the restaurant for which we remove food from menu.", required = true, example = "example@restaurant.com")
            @Valid @PathVariable
            String restaurantEmail,
            @Parameter(description = "Provide food id from yours restaurant menu.")
            @Valid @RequestParam
            String foodId
    ) {
        User user = getCurrentAuthenticatedBusinessUser();
        RestaurantEntity restaurantEntity = getRestaurantByEmail(restaurantEmail);

        if (checkIfCurrentBusinessUserHaveRestaurantWithThisEmail(restaurantEmail, user.getOwner().getRestaurants()) &&
                checkIfRestaurantHasFoodWithProvidedId(foodId, restaurantEntity.getFoodMenu().getFoods())) {
            foodMenuService.deleteFoodFromMenu(Integer.parseInt(foodId), restaurantEmail);
            return ResponseEntity.ok("Food with id: [%s] delete successfully".formatted(foodId));
        } else {
            throw new ResourceNotFoundException("Page not found. Ensure the provided restaurant email: [%s], or food id: [%s] is correct."
                    .formatted(restaurantEmail, foodId));
        }
    }

    private RestaurantEntity getRestaurantByEmail(String restaurantEmail) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantJpaRepository.findByEmail(restaurantEmail);
        return optionalRestaurantEntity.orElseThrow(
                () -> new EntityNotFoundException("RestaurantEntity with email: [%s] not found.".formatted(restaurantEmail)));
    }

    private User getCurrentAuthenticatedBusinessUser() {
        Optional<User> optionalUser = userService.getCurrentOptionalUser();
        return optionalUser.orElseThrow(
                () -> new UnauthorizedException("User is not authenticated. Please log in to access this resource"));
    }

    private boolean checkIfRestaurantHasFoodWithProvidedId(String foodId, List<FoodEntity> foods) {
        int parsedFoodId = Integer.parseInt(foodId);
        return foods.stream()
                .anyMatch(entity -> entity.getFoodId().equals(parsedFoodId));
    }

    private boolean checkIfCurrentBusinessUserHaveRestaurantWithThisEmail(String restaurantEmail, List<Restaurant> userRestaurants) {
        return userRestaurants.stream()
                .anyMatch(restaurant -> restaurant.getRestaurantEmail().equals(restaurantEmail));
    }
}
