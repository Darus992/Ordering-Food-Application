package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockMultipartFile;
import pl.dariuszgilewicz.api.dto.request.RestaurantRequestFormDTO;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import java.time.DayOfWeek;

@UtilityClass
public class RestaurantRequestFormFixtures {

    public static RestaurantRequestForm someRestaurantRequestForm1() {
        RestaurantRequestForm restaurantRequestForm = new RestaurantRequestForm();
        restaurantRequestForm.setRestaurantName("Karczma");
        restaurantRequestForm.setRestaurantPhone("782906225");
        restaurantRequestForm.setRestaurantEmail("karczma@restauracja.pl");
        restaurantRequestForm.setRestaurantAddressCity("Kraków");
        restaurantRequestForm.setRestaurantAddressDistrict("Olsza");
        restaurantRequestForm.setRestaurantAddressPostalCode("22-123");
        restaurantRequestForm.setRestaurantAddressStreet("Orszakowa 78");
        restaurantRequestForm.setOpeningHour("10:00");
        restaurantRequestForm.setCloseHour("18:00");
        restaurantRequestForm.setDayOfWeekFrom(DayOfWeek.MONDAY);
        restaurantRequestForm.setDayOfWeekTill(DayOfWeek.FRIDAY);
        return restaurantRequestForm;
    }

    public static RestaurantRequestForm someRestaurantRequestForm2() {
        RestaurantRequestForm restaurantRequestForm = new RestaurantRequestForm();
        restaurantRequestForm.setRestaurantImageCard(new MockMultipartFile("cardImage.jpg", new byte[0]));
        restaurantRequestForm.setRestaurantImageHeader(new MockMultipartFile("headerImage.jpg", new byte[0]));
        restaurantRequestForm.setRestaurantName("Zapiecek");
        restaurantRequestForm.setRestaurantPhone("441781120");
        restaurantRequestForm.setRestaurantEmail("zapiecek@restaurant.pl");
        restaurantRequestForm.setRestaurantAddressCity("Warszawa");
        restaurantRequestForm.setRestaurantAddressDistrict("Zacisze");
        restaurantRequestForm.setRestaurantAddressPostalCode("11-253");
        restaurantRequestForm.setRestaurantAddressStreet("Lisia 2");
        restaurantRequestForm.setOpeningHour("15:00");
        restaurantRequestForm.setCloseHour("23:00");
        restaurantRequestForm.setDayOfWeekFrom(DayOfWeek.MONDAY);
        restaurantRequestForm.setDayOfWeekTill(DayOfWeek.SATURDAY);
        return restaurantRequestForm;
    }

    public static RestaurantRequestFormDTO someRestaurantRequestFormDTO2() {
        RestaurantRequestFormDTO restaurantRequestFormDTO = new RestaurantRequestFormDTO();
        restaurantRequestFormDTO.setRestaurantImageCard(new MockMultipartFile("cardImage.jpg", new byte[0]));
        restaurantRequestFormDTO.setRestaurantImageHeader(new MockMultipartFile("headerImage.jpg", new byte[0]));
        restaurantRequestFormDTO.setRestaurantName("Zapiecek");
        restaurantRequestFormDTO.setRestaurantPhone("4417811209");
        restaurantRequestFormDTO.setRestaurantEmail("zapiecek@restaurant.pl");
        restaurantRequestFormDTO.setRestaurantAddressCity("Warszawa");
        restaurantRequestFormDTO.setRestaurantAddressDistrict("Zacisze");
        restaurantRequestFormDTO.setRestaurantAddressPostalCode("11-253");
        restaurantRequestFormDTO.setRestaurantAddressStreet("Lisia 2");
        restaurantRequestFormDTO.setOpeningHour("15:00");
        restaurantRequestFormDTO.setCloseHour("23:00");
        restaurantRequestFormDTO.setDayOfWeekFrom(DayOfWeek.MONDAY);
        restaurantRequestFormDTO.setDayOfWeekTill(DayOfWeek.SATURDAY);
        return restaurantRequestFormDTO;
    }
}
