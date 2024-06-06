package pl.dariuszgilewicz.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dariuszgilewicz.api.dto.CustomerUserDTO;
import pl.dariuszgilewicz.api.dto.OrderDTO;
import pl.dariuszgilewicz.api.dto.mapper.CustomerUserMapper;
import pl.dariuszgilewicz.api.dto.mapper.OrderMapper;
import pl.dariuszgilewicz.infrastructure.database.repository.OrderRepository;
import pl.dariuszgilewicz.infrastructure.model.Orders;
import pl.dariuszgilewicz.infrastructure.model.exception.UnauthorizedException;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/customer")
@Tag(name = "Customer", description = "Endpoints for managing customer details")
public class CustomerRestController {

    private final UserService userService;
    private final CustomerUserMapper customerUserMapper;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;


    @Operation(
            summary = "Get current customer details",
            description = "Retrieve the details of the currently authenticated customer."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer details retrieved successfully", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = CustomerUserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Customer not authenticated", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<?> showCustomerDetails() {
        User user = getCurrentAuthenticatedCustomerUser();
        CustomerUserDTO customerUserDTO = customerUserMapper.mapToDTO(user);
        return ResponseEntity.ok(customerUserDTO);
    }


    @Operation(
            summary = "Retrieve customer's order history",
            description = "Fetches the list of orders that the currently authenticated customer has placed."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of customer orders history", content = @Content(
                            mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Customer not authenticated", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/orders-history")
    public ResponseEntity<?> showCustomerOrdersHistory() {
        User user = getCurrentAuthenticatedCustomerUser();
        List<OrderDTO> orderDTOList = orderMapper.mapToDTOList(user.getCustomer().getCustomerOrders());
        return ResponseEntity.ok(orderDTOList);
    }


    @Operation(
            summary = "Show details of a specific order by order number",
            description = "Returns the details of the order associated with the given order number."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order details retrieved successfully", content = @Content(
                            mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @GetMapping("/order/{orderNumber}/details")
    public ResponseEntity<?> showCustomerOrderDetails(
            @Parameter(description = "Order number of the order to retrieve details for", required = true, example = "12345")
            @PathVariable String orderNumber
    ) {
        User user = getCurrentAuthenticatedCustomerUser();
        Orders order = orderRepository.findOrderByOrderNumber(Integer.parseInt(orderNumber));
        OrderDTO orderDTO = orderMapper.mapToDTO(order);
        return ResponseEntity.ok(orderDTO);
    }


    @Operation(
            summary = "Update customer profile details",
            description = "Updates the profile details of the authenticated customer. The password can be optionally updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PatchMapping("/update-profile")
    public ResponseEntity<?> updateCustomerDetails(
            @Parameter(description = "Updated customer details", required = true)
            @Valid @RequestBody CustomerUserDTO customerUserDTO,
            @Parameter(description = "New password for the customer. Leave blank to keep the current password.")
            @RequestParam(required = false) String password
    ) {
        User user = getCurrentAuthenticatedCustomerUser();
        User updatedUserData = customerUserMapper.mapFromDTO(customerUserDTO);
        updatedUserData.setPassword(password);

        userService.updateUserData(updatedUserData, user.getEmail());
        return ResponseEntity.ok("User profile updated successfully.");
    }

    private User getCurrentAuthenticatedCustomerUser() {
        Optional<User> optionalUser = userService.getCurrentOptionalUser();
        return optionalUser.orElseThrow(
                () -> new UnauthorizedException("User is not authenticated. Please log in to access this resource"));
    }
}
