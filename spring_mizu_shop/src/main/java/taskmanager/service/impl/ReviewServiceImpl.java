package taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taskmanager.dto.ReviewDTO;
import taskmanager.dto.ReviewRequest;
import taskmanager.mapper.ReviewMapper;
import taskmanager.model.Product;
import taskmanager.model.Review;
import taskmanager.model.User;
import taskmanager.repository.ProductRepository;
import taskmanager.repository.ReviewRepository;
import taskmanager.repository.UserRepository;
import taskmanager.service.ReviewService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewDTO addReview(ReviewRequest request) {
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();

        return reviewMapper.toDTO(reviewRepo.save(review));
    }

    @Override
    public List<ReviewDTO> getReviewsByProduct(Integer productId) {
        return reviewRepo.findByProductIdAndIsActiveTrue(productId).stream()
                .map(reviewMapper::toDTO)
                .toList();
    }
}
