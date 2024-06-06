package pl.dariuszgilewicz.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for restaurant details")
public class RestaurantDTO {

    @Schema(description = "Restaurant image card", example = "http://localhost:8190/ordering-food-application/image/example@restaurant.com?image=CARD")
    private String restaurantImageCard;

    @Schema(description = "Restaurant image header", example = "http://localhost:8190/ordering-food-application/image/example@restaurant.com?image=HEADER")
    private String restaurantImageHeader;

    @Schema(description = "Restaurant name", example = "Delicious Bites")
    private String restaurantName;

    @Schema(description = "Restaurant phone", example = "123369852")
    private String restaurantPhone;

    @Schema(description = "Restaurant email", example = "info@deliciousbites.com")
    private String restaurantEmail;

    @Schema(description = "Restaurant city", example = "New York")
    private String restaurantCity;

    @Schema(description = "Restaurant district")
    private String restaurantDistrict;

    @Schema(description = "Restaurant postal code", example = "10-001")
    private String restaurantPostalCode;

    @Schema(description = "Restaurant street", example = "Broadway 123")
    private String restaurantAddressStreet;

    @Schema(description = "Restaurant open hour", example = "10:00")
    private String openingHour;

    @Schema(description = "Restaurant close hour", example = "20:00")
    private String closeHour;

    @Schema(description = "Day of the week from which the restaurant is open", example = "MONDAY")
    private String dayOfWeekFrom;

    @Schema(description = "Day of the week until which the restaurant is open", example = "FRIDAY")
    private String dayOfWeekTill;

    @Schema(description = "List of orders for a given restaurant",
            example = """
                    [
                          201537,
                          321554,
                          851626,
                          587265
                        ]
                    """)
    private List<Integer> customerOrdersNumbers;
}
