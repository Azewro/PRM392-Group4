package taskmanager.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePromotionRequest {
    private String code;
    private Integer discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minOrderValue;
}
