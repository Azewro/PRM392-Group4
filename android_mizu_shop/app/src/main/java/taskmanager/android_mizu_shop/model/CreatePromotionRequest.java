package taskmanager.android_mizu_shop.model;

import java.math.BigDecimal;

public class CreatePromotionRequest {
    private String code;
    private Integer discountPercent;
    private String startDate;
    private String endDate;
    private BigDecimal minOrderValue;

    public CreatePromotionRequest() {}

    public CreatePromotionRequest(String code, Integer discountPercent, String startDate, String endDate, BigDecimal minOrderValue) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minOrderValue = minOrderValue;
    }

    // Getter & Setter
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Integer getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Integer discountPercent) { this.discountPercent = discountPercent; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public BigDecimal getMinOrderValue() { return minOrderValue; }
    public void setMinOrderValue(BigDecimal minOrderValue) { this.minOrderValue = minOrderValue; }
} 