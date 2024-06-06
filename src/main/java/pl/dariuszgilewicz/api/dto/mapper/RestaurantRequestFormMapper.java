package pl.dariuszgilewicz.api.dto.mapper;

import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.api.dto.request.RestaurantRequestFormDTO;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import java.io.IOException;

@Component
public class RestaurantRequestFormMapper {

    public RestaurantRequestForm mapFromDTO(RestaurantRequestFormDTO dto) throws IOException {
        RestaurantRequestForm requestForm = new RestaurantRequestForm();
        requestForm.setUsername(dto.getUsername());
        requestForm.setUserEmail(dto.getUserEmail());
        requestForm.setUserPassword(dto.getUserPassword());
        requestForm.setRestaurantImageCard(dto.getRestaurantImageCard());
        requestForm.setRestaurantImageHeader(dto.getRestaurantImageHeader());
        requestForm.setRestaurantName(dto.getRestaurantName());
        requestForm.setRestaurantPhone(dto.getRestaurantPhone());
        requestForm.setRestaurantEmail(dto.getRestaurantEmail());
        requestForm.setRestaurantAddressCity(dto.getRestaurantAddressCity());
        requestForm.setRestaurantAddressDistrict(dto.getRestaurantAddressDistrict());
        requestForm.setRestaurantAddressPostalCode(dto.getRestaurantAddressPostalCode());
        requestForm.setRestaurantAddressStreet(dto.getRestaurantAddressStreet());
        requestForm.setOpeningHour(dto.getOpeningHour());
        requestForm.setCloseHour(dto.getCloseHour());
        requestForm.setDayOfWeekFrom(dto.getDayOfWeekFrom());
        requestForm.setDayOfWeekTill(dto.getDayOfWeekTill());
        return requestForm;
    }
}
