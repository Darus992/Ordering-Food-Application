package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.CustomerEntity;
import pl.dariuszgilewicz.infrastructure.model.Customer;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static pl.dariuszgilewicz.util.CustomerFixtures.someCustomerEntity1;
import static pl.dariuszgilewicz.util.CustomerFixtures.someCustomerModel1;
import static pl.dariuszgilewicz.util.CustomerRequestFormFixtures.someCustomerRequestForm;
import static pl.dariuszgilewicz.util.OrdersFixtures.someOrdersEntityList2;
import static pl.dariuszgilewicz.util.OrdersFixtures.someOrdersModelList2;

@ExtendWith(MockitoExtension.class)
class CustomerEntityMapperTest {

    @InjectMocks
    private CustomerEntityMapper customerEntityMapper;

    @Mock
    private AddressEntityMapper addressEntityMapper;

    @Mock
    private OrderEntityMapper orderEntityMapper;

    @Test
    void mapFromEntity_shouldWorkSuccessfully() {
        //  given
        CustomerEntity customerEntity = someCustomerEntity1();
        Customer expectedCustomer = someCustomerModel1();

        customerEntity.setCustomerOrders(someOrdersEntityList2());
        expectedCustomer.setCustomerOrders(someOrdersModelList2());

        when(addressEntityMapper.mapFromEntity(customerEntity.getAddress())).thenReturn(expectedCustomer.getAddress());
        when(orderEntityMapper.mapFromEntityList(customerEntity.getCustomerOrders())).thenReturn(expectedCustomer.getCustomerOrders());

        //  when
        Customer resultCustomer = customerEntityMapper.mapFromEntity(customerEntity);

        //  then
        assertEquals(expectedCustomer, resultCustomer);
    }

    @Test
    void mapToEntity_shouldWorkSuccessfully() {
        //  given
        CustomerEntity expectedCustomerEntity = someCustomerEntity1();
        Customer customer = someCustomerModel1();

        when(addressEntityMapper.mapToEntity(customer.getAddress())).thenReturn(expectedCustomerEntity.getAddress());

        //  when
        CustomerEntity resultCustomerEntity = customerEntityMapper.mapToEntity(customer);

        //  then
        assertEquals(expectedCustomerEntity, resultCustomerEntity);
    }

    @Test
    void mapFromCustomerRequest_shouldWorkSuccessfully() {
        //  given
        CustomerEntity expectedCustomerEntity = someCustomerEntity1();
        CustomerRequestForm requestForm = someCustomerRequestForm();

        when(addressEntityMapper.mapFromCustomerRequest(requestForm)).thenReturn(expectedCustomerEntity.getAddress());

        //  when
        CustomerEntity resultCustomerEntity = customerEntityMapper.mapFromCustomerRequest(requestForm);

        //  then
        assertEquals(expectedCustomerEntity, resultCustomerEntity);
    }
}