
package taskmanager.mapper;

import org.mapstruct.Mapper;
import taskmanager.dto.CategoryDTO;
import taskmanager.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);
    Category toEntity(CategoryDTO categoryDTO);
}
