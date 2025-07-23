package taskmanager.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PromotionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String code;
    private Integer discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minOrderValue;
    private Boolean isActive;


    public static final class PromotionDTOBuilder {
        private Integer id;
        private String code;
        private Integer discountPercent;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal minOrderValue;
        private Boolean isActive;

        private PromotionDTOBuilder() {
        }

        public static PromotionDTOBuilder builder() {
            return new PromotionDTOBuilder();
        }

        public PromotionDTOBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public PromotionDTOBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public PromotionDTOBuilder withDiscountPercent(Integer discountPercent) {
            this.discountPercent = discountPercent;
            return this;
        }

        public PromotionDTOBuilder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public PromotionDTOBuilder withEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public PromotionDTOBuilder withMinOrderValue(BigDecimal minOrderValue) {
            this.minOrderValue = minOrderValue;
            return this;
        }

        public PromotionDTOBuilder withIsActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public PromotionDTO build() {
            PromotionDTO promotionDTO = new PromotionDTO();
            promotionDTO.isActive = this.isActive;
            promotionDTO.id = this.id;
            promotionDTO.startDate = this.startDate;
            promotionDTO.endDate = this.endDate;
            promotionDTO.minOrderValue = this.minOrderValue;
            promotionDTO.code = this.code;
            promotionDTO.discountPercent = this.discountPercent;
            return promotionDTO;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}

