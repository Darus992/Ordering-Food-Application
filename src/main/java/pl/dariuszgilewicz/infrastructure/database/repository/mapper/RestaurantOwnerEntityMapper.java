package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.dariuszgilewicz.infrastructure.database.entity.*;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;
import pl.dariuszgilewicz.infrastructure.request_form.BusinessRequestForm;

@Component
@AllArgsConstructor
public class RestaurantOwnerEntityMapper {
    private RestaurantEntityMapper restaurantEntityMapper;


    public RestaurantOwner mapFromEntity(RestaurantOwnerEntity entity){
        return RestaurantOwner.builder()
                .name(entity.getName())
                .surname(entity.getSurname())
                .pesel(entity.getPesel())
                .restaurants(restaurantEntityMapper.mapFromEntityList(entity.getRestaurants()))
                .build();
    }

    public RestaurantOwnerEntity mapToEntity(RestaurantOwner owner) {
        return RestaurantOwnerEntity.builder()
                .name(owner.getName())
                .surname(owner.getSurname())
                .pesel(owner.getPesel())
                .restaurants(restaurantEntityMapper.mapToEntityList(owner.getRestaurants()))
                .build();
    }

    public RestaurantOwnerEntity mapFromBusinessRequest(BusinessRequestForm requestForm){
        return RestaurantOwnerEntity.builder()
                .name(requestForm.getOwnerName())
                .surname(requestForm.getOwnerSurname())
                .pesel(requestForm.getOwnerPesel())
                .build();
    }
}
