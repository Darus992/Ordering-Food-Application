package pl.dariuszgilewicz.infrastructure.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Food {

    private Integer foodId;
    private String foodImage;

    @NotEmpty(message = "Category is required.")
    private String category;

    @NotEmpty(message = "Name is required.")
    private String name;

    private String description;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0", message = "Price must be greater than or equal to 0.")
    private BigDecimal price;

    private MultipartFile fileImageToUpload;
}
