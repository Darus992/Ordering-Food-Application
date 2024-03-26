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
                        .city(customer.getAddress().getCity())
                        .district(customer.getAddress().getDistrict())
                        .postalCode(customer.getAddress().getPostalCode())
                        .address(customer.getAddress().getAddressStreet())
                        .build())
                .build();
    }

    default Customer mapFromEntity(CustomerEntity entity){
        return Customer.builder()
                .name(entity.getName())
                .surname(entity.getSurname())
                .phone(entity.getPhone())
                .address(Address.builder()
                        .city(entity.getAddress().getCity())
                        .district(entity.getAddress().getDistrict())
                        .postalCode(entity.getAddress().getPostalCode())
                        .addressStreet(entity.getAddress().getAddress())
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
