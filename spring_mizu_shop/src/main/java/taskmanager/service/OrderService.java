package taskmanager.service;

import taskmanager.dto.OrderRequest;
import taskmanager.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest request);
    List<OrderResponse> getOrdersByUser(Integer userId);
}
