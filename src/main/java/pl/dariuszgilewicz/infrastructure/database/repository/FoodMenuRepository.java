package pl.dariuszgilewicz.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.dariuszgilewicz.infrastructure.database.entity.FoodMenuEntity;
import pl.dariuszgilewicz.infrastructure.database.repository.jpa.FoodMenuJpaRepository;
import pl.dariuszgilewicz.infrastructure.database.repository.mapper.FoodMenuEntityMapper;
import pl.dariuszgilewicz.infrastructure.model.FoodMenu;

@Repository
@AllArgsConstructor
public class FoodMenuRepository {

    private FoodMenuJpaRepository foodMenuJpaRepository;
    private FoodMenuEntityMapper foodMenuEntityMapper;

    public FoodMenuEntity createFoodMenu(FoodMenu foodMenu){
        FoodMenuEntity toSave = foodMenuEntityMapper.mapToEntity(foodMenu);
        foodMenuJpaRepository.save(toSave);
        return toSave;
    }

    public FoodMenuEntity findById(Integer foodMenuId) {
        return foodMenuJpaRepository.findById(Long.parseLong(foodMenuId.toString()))
                .orElseThrow(() -> new EntityNotFoundException("Food Menu with id: [%s] not found".formatted(foodMenuId)));
    }

    public void save(FoodMenuEntity menuEntity) {
        foodMenuJpaRepository.save(menuEntity);
    }

    public FoodMenuEntity findByMenuName(String menuName) {
        return foodMenuJpaRepository.findByMenuName(menuName)
                .orElseThrow(() -> new EntityNotFoundException("Food Menu with menu name: [%s] not found".formatted(menuName)));
    }
}
