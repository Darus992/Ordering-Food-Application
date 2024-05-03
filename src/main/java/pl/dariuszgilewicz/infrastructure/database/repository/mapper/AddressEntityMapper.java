package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.model.Address;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

@Component
@AllArgsConstructor
public class AddressEntityMapper {

    public Address mapFromEntity(AddressEntity entity){
        return Address.builder()
                .city(entity.getCity())
                .district(entity.getDistrict())
                .postalCode(entity.getPostalCode())
                .addressStreet(entity.getAddress())
                .build();
    }

    public AddressEntity mapToEntity(Address address) {
        return AddressEntity.builder()
                .city(address.getCity())
                .district(address.getDistrict())
                .postalCode(address.getPostalCode())
                .address(address.getAddressStreet())
                .build();
    }

    public AddressEntity mapFromBusinessRequest(BusinessRequestForm requestForm){
        return AddressEntity.builder()
                .city(requestForm.getRestaurantAddressCity())
                .district(requestForm.getRestaurantAddressDistrict())
                .postalCode(requestForm.getRestaurantAddressPostalCode())
                .address(requestForm.getRestaurantAddressStreet())
                .build();
    }

    public AddressEntity mapFromCustomerRequest(CustomerRequestForm requestForm) {
        return AddressEntity.builder()
                .city(requestForm.getCustomerAddressCity())
                .district(requestForm.getCustomerAddressDistrict())
                .postalCode(requestForm.getCustomerAddressPostalCode())
                .address(requestForm.getCustomerAddressStreet())
                .build();
    }

    public AddressEntity mapFromRestaurantRequest(RestaurantRequestForm requestForm) {
        return AddressEntity.builder()
                .city(requestForm.getRestaurantAddressCity())
                .district(requestForm.getRestaurantAddressDistrict())
                .postalCode(requestForm.getRestaurantAddressPostalCode())
                .address(requestForm.getRestaurantAddressStreet())
                .build();
    }
}
