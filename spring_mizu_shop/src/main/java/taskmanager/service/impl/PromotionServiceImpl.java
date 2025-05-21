package taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taskmanager.dto.CreatePromotionRequest;
import taskmanager.dto.PromotionDTO;
import taskmanager.mapper.PromotionMapper;
import taskmanager.model.Promotion;
import taskmanager.repository.PromotionRepository;
import taskmanager.service.PromotionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promoRepo;
    private final PromotionMapper promoMapper;

    @Override
    public List<PromotionDTO> getAllPromotions() {
        return promoRepo.findAll().stream()
                .filter(Promotion::getIsActive)
                .map(promoMapper::toDTO)
                .toList();
    }

    @Override
    public PromotionDTO createPromotion(CreatePromotionRequest request) {
        Promotion promo = Promotion.builder()
                .code(request.getCode())
                .discountPercent(request.getDiscountPercent())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .minOrderValue(request.getMinOrderValue())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
        return promoMapper.toDTO(promoRepo.save(promo));
    }

    @Override
    public PromotionDTO updatePromotion(Integer id, CreatePromotionRequest request) {
        Promotion promo = promoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mã giảm giá"));

        promo.setCode(request.getCode());
        promo.setDiscountPercent(request.getDiscountPercent());
        promo.setStartDate(request.getStartDate());
        promo.setEndDate(request.getEndDate());
        promo.setMinOrderValue(request.getMinOrderValue());

        return promoMapper.toDTO(promoRepo.save(promo));
    }

    @Override
    public void deactivatePromotion(Integer id) {
        Promotion promo = promoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mã"));
        promo.setIsActive(false);
        promoRepo.save(promo);
    }
}
