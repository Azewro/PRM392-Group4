package taskmanager.android_mizu_shop.model;

import java.util.List;

public class Order {
    private int id;                  // Unique identifier for the order
    private User user;               // User associated with the order
    private double totalAmount;      // Total amount of the order
    private String status;           // Status of the order (e.g., "Completed")
    private boolean isActive;        // Whether the order is active
    private String createdAt;        // Order creation timestamp (ISO 8601 format)
    private String updatedAt;        // Order update timestamp (ISO 8601 format)
    private Promotion promotion;     // Promotion applied to the order
    private List<OrderItem> items;   // List of order items

    // Constructor
    public Order(int id, User user, double totalAmount, String status, boolean isActive, String createdAt, String updatedAt, Promotion promotion, List<OrderItem> items) {
        this.id = id;
        this.user = user;
        this.totalAmount = totalAmount;
        this.status = status;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.promotion = promotion;
        this.items = items;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
