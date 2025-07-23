package taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taskmanager.dto.CreatePromotionRequest;
import taskmanager.dto.PromotionDTO;
import taskmanager.mapper.PromotionMapper;
import taskmanager.mapper.impl.PromotionMapperImpl;
import taskmanager.model.Promotion;
import taskmanager.repository.PromotionRepository;
import taskmanager.service.PromotionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionServiceImpl implements PromotionService {

    public PromotionServiceImpl(PromotionRepository promoRepo) {
        this.promoRepo = promoRepo;
        this.promoMapper = new PromotionMapperImpl();
    }

    private final PromotionRepository promoRepo;
    private final PromotionMapper promoMapper;

    @Override
    public List<PromotionDTO> getAllPromotions() {
        List<Promotion> promotions = promoRepo.findAll();

        promotions.forEach(promo -> {
            if (promo.getIsActive() == null) {
                promo.setIsActive(false);
            }
        });

        return promotions.stream()
                .filter(Promotion::getIsActive)
                .map(promoMapper::toDTO)
                .collect(Collectors.toList());
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
        // 1. Tìm promotion theo id
        Promotion promo = promoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mã khuyến mãi!"));

        // 2. Nếu code mới khác code cũ, kiểm tra trùng code
        if (!promo.getCode().equals(request.getCode()) && promoRepo.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new RuntimeException("Mã khuyến mãi đã tồn tại!");
        }

        // 3. Cập nhật các trường
        promo.setCode(request.getCode());
        promo.setDiscountPercent(request.getDiscountPercent());
        promo.setStartDate(request.getStartDate());
        promo.setEndDate(request.getEndDate());
        promo.setMinOrderValue(request.getMinOrderValue());
        // Không cập nhật isActive và createdAt ở đây

        // 4. Lưu và trả về DTO
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
