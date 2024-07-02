package pl.dariuszgilewicz.api.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.dariuszgilewicz.api.dto.ErrorResponseDTO;
import pl.dariuszgilewicz.business.OrderService;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.FoodRepository;
import pl.dariuszgilewicz.infrastructure.model.exception.UnauthorizedException;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserRole;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/order")
@Tag(name = "Order", description = "Endpoints for managing order creation")
public class OrderRestController {

    private final UserService userService;
    private final OrderService orderService;
    private final FoodRepository foodRepository;


    @Operation(
            summary = "Create a new order",
            description = "Creates a new order for the authenticated customer user. The lengths of `foodsId` and `foodsValues` arrays must be the same."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "The lengths of foodsId and foodsValues arrays are not equal",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @Parameter(description = "Array of food IDs to be ordered", required = true)
            @RequestParam @NotNull Integer[] foodsId,
            @Parameter(description = "Array of quantities for each food ID", required = true)
            @RequestParam @NotNull Integer[] foodsValues,
            @Parameter(description = "Optional notes for the order", required = false)
            @RequestParam(required = false) String orderNotes,
            @Parameter(description = "Email of the restaurant where the order is being placed", required = true)
            @RequestParam @NotNull String restaurantEmail
    ) {
        User user = getCurrentAuthenticatedCustomerUser();

        if (foodsId.length != foodsValues.length) {
            throw new IllegalArgumentException("The lengths of foodsId and foodsValues arrays are not equal. The number of elements in both arrays must be the same.");
        }

        BigDecimal totalPrice = calculateTotalPrice(Arrays.asList(foodsId).toString(), Arrays.asList(foodsValues).toString());
        orderService.createOrderAndReturnOrderNumber(
                foodsId,
                foodsValues,
                totalPrice,
                orderNotes,
                user,
                restaurantEmail
        );
        return ResponseEntity.ok("Order created successfully");
    }

    private User getCurrentAuthenticatedCustomerUser() {
        Optional<User> optionalUser = userService.getCurrentOptionalUser();
        return optionalUser
                .filter(user -> !user.getRole().equals(UserRole.OWNER))
                .orElseThrow(
                () -> new UnauthorizedException("User is not authenticated. Please log in to access this resource"));
    }

    private BigDecimal calculateTotalPrice(String foodsId, String foodsValues) {
        ObjectMapper objectMapper = new ObjectMapper();
        BigDecimal totalPrice = BigDecimal.ZERO;
        Integer[] foodsIdArray;
        Integer[] foodsValuesArray;

        try {
            foodsIdArray = objectMapper.readValue(foodsId, Integer[].class);
            foodsValuesArray = objectMapper.readValue(foodsValues, Integer[].class);

            for (int i = 0; i < foodsIdArray.length; i++) {
                FoodEntity foodEntity = foodRepository.findFoodEntityById(foodsIdArray[i]);
                BigDecimal sum = foodEntity.getPrice().multiply(new BigDecimal(foodsValuesArray[i]));
                totalPrice = totalPrice.add(sum);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return totalPrice;
    }
}
