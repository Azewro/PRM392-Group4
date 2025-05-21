package taskmanager.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taskmanager.dto.CartItemDTO;
import taskmanager.mapper.CartItemMapper;
import taskmanager.model.CartItem;
import taskmanager.model.Product;
import taskmanager.model.User;
import taskmanager.repository.CartItemRepository;
import taskmanager.repository.ProductRepository;
import taskmanager.repository.UserRepository;
import taskmanager.service.CartService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final CartItemMapper cartMapper;

    @Override
    public List<CartItemDTO> getCartByUser(Integer userId) {
        return cartRepo.findByUserId(userId).stream()
                .map(cartMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public CartItemDTO addToCart(Integer userId, Integer productId, Integer quantity) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setAddedAt(LocalDateTime.now());

        return cartMapper.toDTO(cartRepo.save(item));
    }

    @Override
    public CartItemDTO updateQuantity(Integer cartItemId, Integer quantity) {
        CartItem item = cartRepo.findById(cartItemId).orElseThrow(() -> new RuntimeException("Không tìm thấy cart item"));
        item.setQuantity(quantity);
        return cartMapper.toDTO(cartRepo.save(item));
    }

    @Override
    public void removeCartItem(Integer cartItemId) {
        cartRepo.deleteById(cartItemId);
    }

    @Override
    public void clearCart(Integer userId) {
        cartRepo.deleteByUserId(userId);
    }
}
