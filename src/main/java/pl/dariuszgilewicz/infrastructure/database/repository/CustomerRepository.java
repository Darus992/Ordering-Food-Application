package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.CustomerJpaRepository;

@Repository
@AllArgsConstructor
public class CustomerRepository {

    private CustomerJpaRepository customerJpaRepository;

    public CustomerEntity findCustomerEntityByPhone(String phone) {
        return customerJpaRepository.findByPhone(phone)
                .orElseThrow(() -> new EntityNotFoundException("Customer with phone nr: [%s] not fount".formatted(phone)));
    }
}
