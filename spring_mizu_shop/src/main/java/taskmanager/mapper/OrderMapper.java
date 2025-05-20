
package taskmanager.mapper;

import org.mapstruct.Mapper;
import taskmanager.dto.OrderResponse;
import taskmanager.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toDTO(Order order);
    Order toEntity(OrderResponse dto);
}
