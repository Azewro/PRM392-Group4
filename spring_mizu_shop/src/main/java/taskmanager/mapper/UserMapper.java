
package taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import taskmanager.dto.UserDTO;
import taskmanager.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "isActive", target = "isActive")
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}
