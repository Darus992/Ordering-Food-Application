package pl.dariuszgilewicz.api.dto.mapper;

import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.api.dto.BusinessUserDTO;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.security.User;
import pl.dariuszgilewicz.infrastructure.security.UserRole;

import java.util.ArrayList;

@Component
public class BusinessUserMapper {

    public BusinessRequestForm mapFromDTOToBusinessRequestForm(BusinessUserDTO dto) {
        BusinessRequestForm requestForm = new BusinessRequestForm();
        requestForm.setUsername(dto.getUsername());
        requestForm.setUserEmail(dto.getEmail());
        requestForm.setUserPassword(dto.getPassword());
        requestForm.setOwnerName(dto.getOwnerName());
        requestForm.setOwnerSurname(dto.getOwnerSurname());
        requestForm.setOwnerPesel(dto.getOwnerPesel());
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

    public User mapArgumentToUser(String username, String email, String ownerName, String ownerSurname, String password, String ownerPesel, User currentUser) {
        RestaurantOwner restaurantOwner = new RestaurantOwner("", "", "", new ArrayList<>());
        User resultUser = new User("", "", "", UserRole.OWNER, restaurantOwner, null);

        resultUser.setUsername(username == null ? currentUser.getUsername() : username);
        resultUser.setEmail(email == null ? currentUser.getEmail() : email);
        resultUser.setPassword(password == null ? "" : password);


        restaurantOwner.setName(ownerName == null ? currentUser.getOwner().getName() : ownerName);
        restaurantOwner.setSurname(ownerSurname == null ? currentUser.getOwner().getSurname() : ownerSurname);
        restaurantOwner.setPesel(ownerPesel == null ? restaurantOwner.getPesel() : ownerPesel);

        resultUser.setOwner(restaurantOwner);
        return resultUser;
    }
}
