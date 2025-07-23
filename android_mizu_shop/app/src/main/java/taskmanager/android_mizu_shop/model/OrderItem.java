package taskmanager.android_mizu_shop.model;

public class OrderItem {
    private int id;                // Unique identifier for the order item
    private Order order;           // The order to which this item belongs
    private Product product;       // The product associated with this order item
    private int quantity;          // Quantity of the product in the order
    private double priceAtTime;    // Price of the product at the time of the order

    // Constructor
    public OrderItem(int id, Order order, Product product, int quantity, double priceAtTime) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.priceAtTime = priceAtTime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceAtTime() {
        return priceAtTime;
    }

    public void setPriceAtTime(double priceAtTime) {
        this.priceAtTime = priceAtTime;
    }
}

