package taskmanager.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taskmanager.dto.OrderItemDTO;
import taskmanager.dto.OrderRequest;
import taskmanager.dto.OrderResponse;
import taskmanager.mapper.OrderItemMapper;
import taskmanager.mapper.OrderMapper;
import taskmanager.model.*;
import taskmanager.repository.*;
import taskmanager.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepo;
    private final CartItemRepository cartRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final PromotionRepository promoRepo;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        List<CartItem> cartItems = cartRepo.findByUserId(user.getId());
        if (cartItems.isEmpty()) throw new RuntimeException("Giỏ hàng đang trống");

        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Promotion promotion = null;
        if (request.getPromotionCode() != null) {
            promotion = promoRepo.findByCodeAndIsActiveTrue(request.getPromotionCode());
            if (promotion != null && total.compareTo(promotion.getMinOrderValue()) >= 0) {
                BigDecimal discount = total.multiply(BigDecimal.valueOf(promotion.getDiscountPercent())).divide(BigDecimal.valueOf(100));
                total = total.subtract(discount);
            }
        }

        Order order = Order.builder()
                .user(user)
                .totalAmount(total)
                .status("pending")
                .promotion(promotion)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepo.save(order);

        List<OrderItem> items = cartItems.stream().map(cart -> OrderItem.builder()
                .order(savedOrder)
                .product(cart.getProduct())
                .quantity(cart.getQuantity())
                .priceAtTime(cart.getProduct().getPrice())
                .build()).toList();

        orderItemRepo.saveAll(items);
        cartRepo.deleteByUserId(user.getId());

        return OrderResponse.builder()
                .id(savedOrder.getId())
                .totalAmount(savedOrder.getTotalAmount())
                .status(savedOrder.getStatus())
                .createdAt(savedOrder.getCreatedAt())
                .items(items.stream().map(orderItemMapper::toDTO).toList())
                .build();
    }

    @Override
    public List<OrderResponse> getOrdersByUser(Integer userId) {
        return orderRepo.findByUserId(userId).stream()
                .map(order -> OrderResponse.builder()
                        .id(order.getId())
                        .totalAmount(order.getTotalAmount())
                        .status(order.getStatus())
                        .createdAt(order.getCreatedAt())
                        .items(orderItemRepo.findByOrderId(order.getId())
                                .stream().map(orderItemMapper::toDTO).toList())
                        .build())
                .toList();
    }
}
