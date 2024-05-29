package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockMultipartFile;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import java.time.DayOfWeek;

@UtilityClass
public class RestaurantRequestFormFixtures {

    public static RestaurantRequestForm someRestaurantRequestForm1(){
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
//        restaurantRequestForm.setUsername("business_user");
//        restaurantRequestForm.setUserEmail("business@business_user.com");
//        restaurantRequestForm.setUserPassword("haslo");
//        restaurantRequestForm.setOwnerName("Maciek");
//        restaurantRequestForm.setOwnerSurname("Pracowity");
//        restaurantRequestForm.setOwnerPesel("97051242360");
        restaurantRequestForm.setRestaurantImageCard(new MockMultipartFile("cardImage.jpg", new byte[0]));
        restaurantRequestForm.setRestaurantImageHeader(new MockMultipartFile("headerImage.jpg", new byte[0]));
        restaurantRequestForm.setRestaurantName("Zapiecek");
        restaurantRequestForm.setRestaurantPhone("4417811209");
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
}
