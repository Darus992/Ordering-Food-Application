package pl.dariuszgilewicz.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for image file")
public class RestaurantImageRequestDTO {

    @Schema(description = "Image file")
    private MultipartFile requestImage;
}
