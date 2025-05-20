
package taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import taskmanager.dto.ProductResponse;
import taskmanager.dto.CreateProductRequest;
import taskmanager.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponse toResponse(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(CreateProductRequest dto);
}
