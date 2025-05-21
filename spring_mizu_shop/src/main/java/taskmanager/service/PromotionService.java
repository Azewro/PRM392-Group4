package taskmanager.service;

import taskmanager.dto.CreatePromotionRequest;
import taskmanager.dto.PromotionDTO;

import java.util.List;

public interface PromotionService {
    List<PromotionDTO> getAllPromotions();
    PromotionDTO createPromotion(CreatePromotionRequest request);
    PromotionDTO updatePromotion(Integer id, CreatePromotionRequest request);
    void deactivatePromotion(Integer id);
}
