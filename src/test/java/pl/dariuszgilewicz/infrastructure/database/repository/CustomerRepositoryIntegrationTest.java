package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import pl.dariuszgilewicz.configuration.AbstractJpaIT;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.CustomerJpaRepository;
import pl.dariuszgilewicz.infrastructure.security.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pl.dariuszgilewicz.util.CustomerFixtures.someCustomerEntity1;
import static pl.dariuszgilewicz.util.CustomerFixtures.someCustomerModel2;
import static pl.dariuszgilewicz.util.UsersFixtures.someCustomerUserModel1;

@Import(CustomerRepository.class)
class CustomerRepositoryIntegrationTest extends AbstractJpaIT {

    @Autowired
    private CustomerJpaRepository jpaRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void updateCustomer_shouldWorkSuccessfully() {
        //  given
        CustomerEntity existingCustomerEntity = someCustomerEntity1();
        User userForm = someCustomerUserModel1();
        userForm.setCustomer(someCustomerModel2());

        jpaRepository.save(existingCustomerEntity);

        //  when
        customerRepository.updateCustomer(existingCustomerEntity, userForm);

        //  then
        //  verify persistence
        CustomerEntity persistenceCustomer = jpaRepository.findById(existingCustomerEntity.getCustomerId()).orElseThrow();
        List<CustomerEntity> all = jpaRepository.findAll();

        assertNotNull(persistenceCustomer);
        assertEquals(userForm.getCustomer().getName(), persistenceCustomer.getName());
        assertEquals(userForm.getCustomer().getSurname(), persistenceCustomer.getSurname());
        assertEquals(userForm.getCustomer().getPhone(), persistenceCustomer.getPhone());

        assertEquals(userForm.getCustomer().getAddress().getCity(), persistenceCustomer.getAddress().getCity());
        assertEquals(userForm.getCustomer().getAddress().getDistrict(), persistenceCustomer.getAddress().getDistrict());
        assertEquals(userForm.getCustomer().getAddress().getPostalCode(), persistenceCustomer.getAddress().getPostalCode());
        assertEquals(userForm.getCustomer().getAddress().getAddressStreet(), persistenceCustomer.getAddress().getAddress());
        assertEquals(1, all.size());
    }

    @Test
    void updateCustomer_shouldThrowExceptionWhenUserFormHasFileWithNullValue() {
        // given
        CustomerEntity customerEntity = someCustomerEntity1();

        User userForm = someCustomerUserModel1();
        userForm.getCustomer().setName(null);

        // when
        // then
        assertThrows(RuntimeException.class, () -> customerRepository.updateCustomer(customerEntity, userForm));
    }

    @Test
    void findCustomerEntityByPhone_shouldWorkSuccessfully() {
        //  given
        CustomerEntity expectedCustomerEntity = someCustomerEntity1();
        String phone = "154457147";

        jpaRepository.save(expectedCustomerEntity);

        //  when
        CustomerEntity foundedCustomerEntity = customerRepository.findCustomerEntityByPhone(phone);

        //  then
        assertNotNull(foundedCustomerEntity);
        assertEquals(expectedCustomerEntity, foundedCustomerEntity);

        //  verify persistence
        CustomerEntity persistenceEntity = jpaRepository.findByPhone(phone).orElseThrow();
        List<CustomerEntity> all = jpaRepository.findAll();

        assertNotNull(persistenceEntity);
        assertEquals(foundedCustomerEntity, persistenceEntity);
        assertEquals(1, all.size());
    }

    @Test
    void findCustomerEntityByPhone_shouldThrowExceptionWhenPhoneIsNotFound() {
        //  given
        String phone = "100455142";

        //  when
        //  then
        assertThrows(EntityNotFoundException.class, () -> customerRepository.findCustomerEntityByPhone(phone));

        //  verify persistence
        List<CustomerEntity> all = jpaRepository.findAll();
        assertEquals(0, all.size());
    }
}