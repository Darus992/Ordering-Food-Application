package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.model.Address;
import pl.dariuszgilewicz.infrastructure.model.Customer;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerEntityMapper {


    default CustomerEntity mapToEntity(Customer customer){
        return CustomerEntity.builder()
                .name(customer.getName())
                .surname(customer.getSurname())
                .phone(customer.getPhone())
                .address(AddressEntity.builder()
                        .city(customer.getAddress().getAddressCity())
                        .district(customer.getAddress().getAddressDistrict())
                        .postalCode(customer.getAddress().getAddressPostalCode())
                        .address(customer.getAddress().getAddressAddressStreet())
                        .build())
                .build();
    }

    default Customer mapFromEntity(CustomerEntity entity){
        return Customer.builder()
                .name(entity.getName())
                .surname(entity.getSurname())
                .phone(entity.getPhone())
                .address(Address.builder()
                        .addressCity(entity.getAddress().getCity())
                        .addressDistrict(entity.getAddress().getDistrict())
                        .addressPostalCode(entity.getAddress().getPostalCode())
                        .addressAddressStreet(entity.getAddress().getAddress())
                        .build())
                .build();
    }



    default CustomerEntity mapFromRequest(CustomerRequestForm customerRequestForm){
        return CustomerEntity.builder()
                .name(customerRequestForm.getCustomerName())
                .surname(customerRequestForm.getCustomerSurname())
                .phone(customerRequestForm.getCustomerPhone())
                .address(AddressEntity.builder()
                        .city(customerRequestForm.getCustomerAddressCity())
                        .district(customerRequestForm.getCustomerAddressDistrict())
                        .postalCode(customerRequestForm.getCustomerAddressPostalCode())
                        .address(customerRequestForm.getCustomerAddressStreet())
                        .build())
                .build();
    }
}
