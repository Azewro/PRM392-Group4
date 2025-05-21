package taskmanager.service;

import taskmanager.dto.CartItemDTO;

import java.util.List;

public interface CartService {
    List<CartItemDTO> getCartByUser(Integer userId);
    CartItemDTO addToCart(Integer userId, Integer productId, Integer quantity);
    CartItemDTO updateQuantity(Integer cartItemId, Integer quantity);
    void removeCartItem(Integer cartItemId);
    void clearCart(Integer userId);
}
