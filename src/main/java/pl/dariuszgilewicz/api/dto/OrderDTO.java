package pl.dariuszgilewicz.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import pl.dariuszgilewicz.api.dto.request.OrderFoodRequestDTO;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object representing an order")
public class OrderDTO {

    @Schema(description = "A uniquely generated random order number", example = "15426")
    private String orderNumber;

    @Schema(description = "Current order status", example = "IN_PROGRESS")
    private String orderStatus;

    @Schema(description = "Additional customer request", example = "The doorbell doesn't work.")
    private String orderNotes;

    @Schema(description = "Date and time of placing the order", example = "2021-04-21 16:28:00")
    private String orderReceivedDateTime;

    @Schema(description = "Date and time of delivery of the order.", example = "2021-04-21 17:15:32")
    private String orderCompletedDateTime;

    @Schema(description = "Order content",
            example = """
            [
                  {
                    "food": {
                      "foodImage": "http://localhost:8190/ordering-food-application/image/food/1",
                      "foodCategory": "Pizza",
                      "foodName": "Margherita",
                      "foodDescription": "Classic pizza with tomato sauce, mozzarella cheese, and fresh basil.",
                      "foodPrice": "30.99"
                    },
                    "quantity": "2"
                  }
                ]
            """)
    private List<OrderFoodRequestDTO> orderFoodRequests;

    @Schema(description = "Total price of the order", example = "61.98")
    private String orderTotalPrice;

    @Schema(description = "Name of the customer", example = "John")
    private String customerName;

    @Schema(description = "Surname of the customer", example = "Doe")
    private String customerSurname;

    @Schema(description = "Phone of the customer", example = "741852963")
    private String customerPhone;

    @Schema(description = "Name of the city where the customer lives", example = "Warsaw")
    private String customerAddressCity;

    @Schema(description = "Name of the district in a given city")
    private String customerAddressDistrict;

    @Schema(description = "Postal code for a given city", example = "00-120")
    private String customerAddressPostalCode;

    @Schema(description = "Street name for a given city", example = "Złota 59")
    private String customerAddressStreet;
}
