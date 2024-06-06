package pl.dariuszgilewicz.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.dariuszgilewicz.infrastructure.model.exception.NoContentException;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/image")
@Tag(name = "Image", description = "Endpoints for managing API images")
public class ImageRestController {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final FoodJpaRepository foodJpaRepository;


    @Operation(
            summary = "Get restaurant image",
            description = "Retrieves an image for the specified restaurant based on the email. Optionally, specify 'CARD' to get the card image, otherwise the header image is returned."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image retrieved successfully",
                    content = @Content(mediaType = "image/jpeg")),
            @ApiResponse(responseCode = "204", description = "No content available for the specified restaurant and image type",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content)
    })
    @GetMapping("/{restaurantEmail}")
    public ResponseEntity<?> getImage(
            @Parameter(description = "Email of the restaurant for which to retrieve the image", required = true, example = "example@restaurant.com")
            @PathVariable String restaurantEmail,
            @Parameter(description = "Type of image to retrieve (CARD or HEADER). If not specified, HEADER image is returned")
            @RequestParam(required = false) String image
    ) {
        Optional<RestaurantEntity> optionalRestaurant = restaurantJpaRepository.findByEmail(restaurantEmail);

        if(optionalRestaurant.isPresent()) {
            byte[] imageData;
            if("CARD".equals(image)) {
                imageData = optionalRestaurant.get().getRestaurantImageCard();
            } else {
                imageData = optionalRestaurant.get().getRestaurantImageHeader();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            throw new NoContentException("No content available for: [%s] in restaurant with email: [%s]".formatted(image, restaurantEmail));
        }
    }

    @Operation(
            summary = "Get food image",
            description = "Retrieves an image for the specified food item based on the food ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image retrieved successfully",
                    content = @Content(mediaType = "image/jpeg")),
            @ApiResponse(responseCode = "204", description = "No content available for the specified food ID",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Food not found",
                    content = @Content)
    })
    @GetMapping("/food/{foodId}")
    public ResponseEntity<?> getFoodImage(
            @Parameter(description = "ID of the food item for which to retrieve the image", required = true, example = "1")
            @PathVariable Integer foodId
    ) {
        Optional<FoodEntity> optionalFood = foodJpaRepository.findById(foodId);

        if (optionalFood.isPresent()) {
            byte[] imageData;
            imageData = optionalFood.get().getFoodImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            throw new NoContentException("No content available for foodId: [%s]".formatted(foodId));
        }
    }
}
