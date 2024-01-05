package pl.dariuszgilewicz.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.dariuszgilewicz.infrastructure.database.entity.RestaurantOpeningTimeEntity;
import pl.dariuszgilewicz.infrastructure.model.RestaurantOpeningTime;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantOpeningTimeEntityMapper {

    List<RestaurantOpeningTimeEntity> mapToEntity(List<RestaurantOpeningTime> restaurantOpeningTime);

    List<RestaurantOpeningTime> mapFromEntity(List<RestaurantOpeningTimeEntity> entities);
}
