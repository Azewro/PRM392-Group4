
package taskmanager.mapper;

import org.mapstruct.Mapper;
import taskmanager.dto.PromotionDTO;
import taskmanager.model.Promotion;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    PromotionDTO toDTO(Promotion promotion);
}
