package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOwnerEntity;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOwner;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantOwnerEntityMapper {

    RestaurantOwnerEntity mapToEntity(RestaurantOwner restaurantOwner);
    RestaurantOwner mapFromEntity(RestaurantOwnerEntity entity);
}
