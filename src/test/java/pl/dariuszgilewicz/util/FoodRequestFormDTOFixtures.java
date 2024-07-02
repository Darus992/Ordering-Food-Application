package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockMultipartFile;
import pl.dariuszgilewicz.api.dto.request.FoodRequestFormDTO;

@UtilityClass
public class FoodRequestFormDTOFixtures {

    public static FoodRequestFormDTO someFoodRequestDTO1() {
        byte[] content = "Test content".getBytes();
        MockMultipartFile mockFileImage = new MockMultipartFile("requestFormDTO", "pepsiZero.jpg", "image/jpeg", content);

        return FoodRequestFormDTO.builder()
                .foodCategory("Napoje")
                .foodName("Pepsi Zero")
                .foodDescription("")
                .foodPrice("6.5")
                .fileImageToUpload(mockFileImage)
                .build();
    }
}
