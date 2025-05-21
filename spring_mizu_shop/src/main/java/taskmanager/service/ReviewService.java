package taskmanager.service;

import taskmanager.dto.ReviewDTO;
import taskmanager.dto.ReviewRequest;

import java.util.List;

public interface ReviewService {
    ReviewDTO addReview(ReviewRequest request);
    List<ReviewDTO> getReviewsByProduct(Integer productId);
}
