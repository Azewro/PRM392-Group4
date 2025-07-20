package taskmanager.android_mizu_shop.model;

public class Promotion {
    private Integer id;
    private String code;
    private Double discountPercent;
    private String startDate;
    private String endDate;
    private Double minOrderValue;
    private Boolean isActive;

    public Promotion() {}

    public Promotion(Integer id, String code, Double discountPercent, String startDate, String endDate, Double minOrderValue, Boolean isActive) {
        this.id = id;
        this.code = code;
        this.discountPercent = discountPercent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minOrderValue = minOrderValue;
        this.isActive = isActive;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
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
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
} 