package taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionDTO {
    private Integer id;
    private String code;
    private Integer discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minOrderValue;
    private Boolean isActive;
}

