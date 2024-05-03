package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.model.Customer;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;

@Component
@AllArgsConstructor
public class CustomerEntityMapper {
    private AddressEntityMapper addressEntityMapper;
    private OrderEntityMapper orderEntityMapper;

    public Customer mapFromEntity(CustomerEntity entity) {
        return Customer.builder()
                .name(entity.getName())
                .surname(entity.getSurname())
                .phone(entity.getPhone())
                .address(addressEntityMapper.mapFromEntity(entity.getAddress()))
                .customerOrders(orderEntityMapper.mapFromEntityList(entity.getCustomerOrders()))
                .build();
    }

    public CustomerEntity mapToEntity(Customer customer) {
        return CustomerEntity.builder()
                .name(customer.getName())
                .surname(customer.getSurname())
                .phone(customer.getPhone())
                .address(addressEntityMapper.mapToEntity(customer.getAddress()))
                .build();
    }

    public CustomerEntity mapFromCustomerRequest(CustomerRequestForm requestForm) {
        return CustomerEntity.builder()
                .name(requestForm.getCustomerName())
                .surname(requestForm.getCustomerSurname())
                .phone(requestForm.getCustomerPhone())
                .address(addressEntityMapper.mapFromCustomerRequest(requestForm))
                .build();
    }
}
