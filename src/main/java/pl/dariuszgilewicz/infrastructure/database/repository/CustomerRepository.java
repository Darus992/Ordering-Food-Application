package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.CustomerJpaRepository;
import pl.dariuszgilewicz.infrastructure.security.User;

@Repository
@AllArgsConstructor
public class CustomerRepository {

    private CustomerJpaRepository customerJpaRepository;

    public void updateCustomer(CustomerEntity customerEntity, User userForm) {
        AddressEntity addressEntity = customerEntity.getAddress();

        customerEntity.setName(userForm.getCustomer().getName());
        customerEntity.setSurname(userForm.getCustomer().getSurname());
        customerEntity.setPhone(userForm.getCustomer().getPhone());

        addressEntity.setCity(userForm.getCustomer().getAddress().getCity());
        addressEntity.setDistrict(userForm.getCustomer().getAddress().getDistrict());
        addressEntity.setPostalCode(userForm.getCustomer().getAddress().getPostalCode());
        addressEntity.setAddress(userForm.getCustomer().getAddress().getAddressStreet());
        customerJpaRepository.save(customerEntity);
    }

    public CustomerEntity findCustomerEntityByPhone(String phone){
        return customerJpaRepository.findByPhone(phone)
                .orElseThrow(() -> new EntityNotFoundException("Customer Entity with phone: [%s] not found.".formatted(phone)));
    }
}
