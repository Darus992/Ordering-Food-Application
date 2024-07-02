package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockMultipartFile;
import pl.dariuszgilewicz.api.dto.BusinessUserDTO;

import java.time.DayOfWeek;

@UtilityClass
public class BusinessUserDTOFixtures {

    public static BusinessUserDTO someBusinessUserDTO1() {
        BusinessUserDTO businessUserDTO = BusinessUserDTO.builder()
                .ownerName("Maciek")
                .ownerSurname("Pracowity")
                .ownerPesel("97051242360")
                .restaurantImageCard(new MockMultipartFile("cardImage.jpg", new byte[0]))
                .restaurantImageHeader(new MockMultipartFile("headerImage.jpg", new byte[0]))
                .restaurantName("Zapiecek")
                .restaurantPhone("441781120")
                .restaurantEmail("zapiecek@restaurant.pl")
                .restaurantAddressCity("Warszawa")
                .restaurantAddressDistrict("Zacisze")
                .restaurantAddressPostalCode("11-253")
                .restaurantAddressStreet("Lisia 2")
                .openingHour("15:00")
                .closeHour("23:00")
                .dayOfWeekFrom(DayOfWeek.MONDAY)
                .dayOfWeekTill(DayOfWeek.SATURDAY)
                .build();

        businessUserDTO.setUsername("business_user");
        businessUserDTO.setEmail("business@business_user.com");
        businessUserDTO.setPassword("haslo");

        return businessUserDTO;
    }
}
