
package taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import taskmanager.dto.ReviewDTO;
import taskmanager.model.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "user.username", target = "username")
    ReviewDTO toDTO(Review review);
}
