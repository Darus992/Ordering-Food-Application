package pl.dariuszgilewicz.util;

import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockMultipartFile;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;

import java.time.DayOfWeek;

@UtilityClass
public class BusinessRequestFormFixtures {

    public static BusinessRequestForm someBusinessRequestForm1() {
        BusinessRequestForm businessRequestForm = new BusinessRequestForm();
        businessRequestForm.setUsername("business_user");
        businessRequestForm.setUserEmail("business@business_user.com");
        businessRequestForm.setUserPassword("haslo");
        businessRequestForm.setOwnerName("Zbyszek");
        businessRequestForm.setOwnerSurname("Pracowity");
        businessRequestForm.setOwnerPesel("91014452879");
        businessRequestForm.setRestaurantName("Na Wypasie");
        businessRequestForm.setRestaurantPhone("457781653");
        businessRequestForm.setRestaurantEmail("na_wypasie@restaurant.pl");
        businessRequestForm.setRestaurantAddressCity("Białystok");
        businessRequestForm.setRestaurantAddressDistrict("Antoniuk");
        businessRequestForm.setRestaurantAddressPostalCode("12-221");
        businessRequestForm.setRestaurantAddressStreet("Antoniukowska 100");
        businessRequestForm.setOpeningHour("15:00");
        businessRequestForm.setCloseHour("23:00");
        businessRequestForm.setDayOfWeekFrom(DayOfWeek.MONDAY);
        businessRequestForm.setDayOfWeekTill(DayOfWeek.SATURDAY);
        return businessRequestForm;
    }

    public static BusinessRequestForm someBusinessRequestForm2() {
        BusinessRequestForm businessRequestForm = new BusinessRequestForm();
        businessRequestForm.setUsername("business_user");
        businessRequestForm.setUserEmail("business@business_user.com");
        businessRequestForm.setUserPassword("haslo");
        businessRequestForm.setOwnerName("Maciek");
        businessRequestForm.setOwnerSurname("Pracowity");
        businessRequestForm.setOwnerPesel("97051242360");
        businessRequestForm.setRestaurantName("Na Wypasie");
        businessRequestForm.setRestaurantPhone("457781653");
        businessRequestForm.setRestaurantEmail("na_wypasie@restaurant.pl");
        businessRequestForm.setRestaurantAddressCity("Białystok");
        businessRequestForm.setRestaurantAddressDistrict("Antoniuk");
        businessRequestForm.setRestaurantAddressPostalCode("12-221");
        businessRequestForm.setRestaurantAddressStreet("Antoniukowska 100");
        businessRequestForm.setOpeningHour("15:00");
        businessRequestForm.setCloseHour("23:00");
        businessRequestForm.setDayOfWeekFrom(DayOfWeek.MONDAY);
        businessRequestForm.setDayOfWeekTill(DayOfWeek.SATURDAY);
        return businessRequestForm;
    }



    public static BusinessRequestForm someBusinessRequestForm3() {
        BusinessRequestForm businessRequestForm = new BusinessRequestForm();
//        businessRequestForm.setUsername("business_user");
//        businessRequestForm.setUserEmail("business@business_user.com");
//        businessRequestForm.setUserPassword("haslo");
//        businessRequestForm.setOwnerName("Maciek");
//        businessRequestForm.setOwnerSurname("Pracowity");
//        businessRequestForm.setOwnerPesel("97051242360");
        businessRequestForm.setRestaurantImageCard(new MockMultipartFile("cardImage.jpg", new byte[0]));
        businessRequestForm.setRestaurantImageHeader(new MockMultipartFile("headerImage.jpg", new byte[0]));
        businessRequestForm.setRestaurantName("Zapiecek");
        businessRequestForm.setRestaurantPhone("4417811209");
        businessRequestForm.setRestaurantEmail("zapiecek@restaurant.pl");
        businessRequestForm.setRestaurantAddressCity("Warszawa");
        businessRequestForm.setRestaurantAddressDistrict("Zacisze");
        businessRequestForm.setRestaurantAddressPostalCode("11-253");
        businessRequestForm.setRestaurantAddressStreet("Lisia 2");
        businessRequestForm.setOpeningHour("15:00");
        businessRequestForm.setCloseHour("23:00");
        businessRequestForm.setDayOfWeekFrom(DayOfWeek.MONDAY);
        businessRequestForm.setDayOfWeekTill(DayOfWeek.SATURDAY);
        return businessRequestForm;
    }
}
