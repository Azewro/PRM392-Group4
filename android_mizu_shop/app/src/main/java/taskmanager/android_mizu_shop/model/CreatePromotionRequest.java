package taskmanager.android_mizu_shop.model;

public class CreatePromotionRequest {
    private String code;
    private Double discountPercent;
    private String startDate;
    private String endDate;
    private Double minOrderValue;

    public CreatePromotionRequest() {}

    public CreatePromotionRequest(String code, Double discountPercent, String startDate, String endDate, Double minOrderValue) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minOrderValue = minOrderValue;
    }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public Double getMinOrderValue() { return minOrderValue; }
    public void setMinOrderValue(Double minOrderValue) { this.minOrderValue = minOrderValue; }
} 