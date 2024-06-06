package pl.dariuszgilewicz.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dariuszgilewicz.api.dto.RestaurantDTO;
import pl.dariuszgilewicz.api.dto.mapper.RestaurantMapper;
import pl.dariuszgilewicz.api.dto.mapper.RestaurantRequestFormMapper;
import pl.dariuszgilewicz.api.dto.request.RestaurantRequestFormDTO;
import pl.dariuszgilewicz.business.RestaurantService;
import pl.dariuszgilewicz.infrastructure.model.Restaurant;
import pl.dariuszgilewicz.infrastructure.model.exception.UnauthorizedException;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Tag(name = "Restaurant", description = "Endpoints for managing restaurant details")
public class RestaurantRestController {
    public final String BASE_URL = "http://localhost:8190/ordering-food-application";

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantRequestFormMapper restaurantRequestFormMapper;
    private final UserService userService;


    @Operation(
            summary = "Retrieve restaurant's based on search term",
            description = "Fetches the list of restaurants that were found by the search term."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of the restaurants", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = RestaurantDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantDetailsList(
            @Parameter(description = "City or street of the restaurant to retrieve details for", example = "Warszawa")
            @RequestParam(required = false, defaultValue = "Warszawa")
            String searchTerm
    ) {
        List<Restaurant> restaurantList = restaurantService.findRestaurantsBySearchTerm(searchTerm);
        List<RestaurantDTO> restaurantDTOS = restaurantMapper.mapToDTOList(restaurantList, BASE_URL);
        return ResponseEntity.ok(restaurantDTOS);
    }


    @Operation(
            summary = "Show details of a specific restaurant by restaurant email",
            description = "Return the details of the restaurant associated with the given restaurant email."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant details retrieved successfully", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = RestaurantDTO.class))),
            @ApiResponse(responseCode = "404", description = "Page not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/restaurants/{restaurantEmail}")
    public ResponseEntity<?> getRestaurantDetails(
            @Parameter(description = "Restaurant email of the restaurant to retrieve details for", required = true, example = "info@deliciousbites.com")
            @PathVariable String restaurantEmail
    ) {
        Restaurant restaurant = restaurantService.findRestaurantByEmail(restaurantEmail);
        RestaurantDTO restaurantDTO = restaurantMapper.mapToDTO(restaurant, BASE_URL);
        return ResponseEntity.ok(restaurantDTO);
    }

    @Operation(
            summary = "Create a new restaurant",
            description = "Create a new restaurant for the authenticated owner.",
            requestBody = @RequestBody(content = @Content(mediaType = "multipart/form-data",
                    schema = @Schema(implementation = RestaurantRequestFormDTO.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/restaurant/create")
    public ResponseEntity<?> createRestaurant(
            @Parameter(description = "Restaurant form details")
            @RequestBody RestaurantRequestFormDTO restaurantRequestFormDTO
    ) throws IOException {
        RestaurantRequestForm restaurantRequestForm = restaurantRequestFormMapper.mapFromDTO(restaurantRequestFormDTO);
        Optional<User> optionalUser = userService.getCurrentOptionalUser();

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            restaurantService.createRestaurantAndAssignToOwner(restaurantRequestForm, user.getOwner());
            return ResponseEntity.status(HttpStatus.CREATED).body("Restaurant created successfully.");
        } else {
            throw new UnauthorizedException("User is not authenticated. Please log in to access this resource");
        }
    }
}
