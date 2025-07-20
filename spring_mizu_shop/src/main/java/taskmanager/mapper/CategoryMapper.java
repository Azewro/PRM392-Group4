
package taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import taskmanager.dto.CategoryDTO;
import taskmanager.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    // Map entity -> DTO
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "isActive", source = "isActive")
    CategoryDTO toDTO(Category category);

    // Map DTO -> entity (imageUrl sẽ được set ở service, các trường khác cũng nên set ở service)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "sortOrder", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryDTO categoryDTO);
}
