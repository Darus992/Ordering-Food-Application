package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dariuszgilewicz.infrastructure.database.entity.AddressEntity;
import pl.dariuszgilewicz.infrastructure.model.Address;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.CustomerRequestForm;
import pl.dariuszgilewicz.infrastructure.request_form.RestaurantRequestForm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.dariuszgilewicz.util.AddressFixtures.*;
import static pl.dariuszgilewicz.util.BusinessRequestFormFixtures.someBusinessRequestForm2;
import static pl.dariuszgilewicz.util.CustomerRequestFormFixtures.someCustomerRequestForm;
import static pl.dariuszgilewicz.util.RestaurantRequestFormFixtures.someRestaurantRequestForm1;

@ExtendWith(MockitoExtension.class)
class AddressEntityMapperTest {

    @InjectMocks
    private AddressEntityMapper addressEntityMapper;


    @Test
    void mapFromEntity_shouldWorkSuccessfully() {
        //  given
        Address expectedAddress = someAddressModel1();
        AddressEntity addressEntity = someAddressEntity1();

        //  when
        Address resultAddress = addressEntityMapper.mapFromEntity(addressEntity);

        //  then
        assertEquals(expectedAddress, resultAddress);

    }

    @Test
    void mapToEntity_shouldWorkSuccessfully() {
        //  given
        AddressEntity expectedAddressEntity = someAddressEntity2();
        Address address = someAddressModel2();

        //  when
        AddressEntity resultAddressEntity = addressEntityMapper.mapToEntity(address);

        //  then
        assertEquals(expectedAddressEntity, resultAddressEntity);
    }

    @Test
    void mapFromBusinessRequest_shouldWorkSuccessfully() {
        //  given
        BusinessRequestForm requestForm = someBusinessRequestForm2();
        AddressEntity expectedAddressEntity = someAddressEntity1();

        //  when
        AddressEntity resultAddressEntity = addressEntityMapper.mapFromBusinessRequest(requestForm);

        //  then
        assertEquals(expectedAddressEntity, resultAddressEntity);
    }

    @Test
    void mapFromCustomerRequest_shouldWorkSuccessfully() {
        //  given
        CustomerRequestForm requestForm = someCustomerRequestForm();
        AddressEntity expectedAddressEntity = someAddressEntity2();

        //  when
        AddressEntity resultAddressEntity = addressEntityMapper.mapFromCustomerRequest(requestForm);

        //  then
        assertEquals(expectedAddressEntity, resultAddressEntity);
    }

    @Test
    void mapFromRestaurantRequest_shouldWorkSuccessfully() {
        //  given
        RestaurantRequestForm requestForm = someRestaurantRequestForm1();
        AddressEntity expectedAddressEntity = someAddressEntity3();

        //  when
        AddressEntity resultAddressEntity = addressEntityMapper.mapFromRestaurantRequest(requestForm);

        //  then
        assertEquals(expectedAddressEntity, resultAddressEntity);
    }
}