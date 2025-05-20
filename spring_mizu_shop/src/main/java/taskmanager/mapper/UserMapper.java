
package taskmanager.mapper;

import org.mapstruct.Mapper;
import taskmanager.dto.UserDTO;
import taskmanager.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}
