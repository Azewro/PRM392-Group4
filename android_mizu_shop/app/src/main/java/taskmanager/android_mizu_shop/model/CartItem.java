package taskmanager.android_mizu_shop.model;

public class CartItem {
    private int id;             // cart_item.id
    private int userId;         // cart_item.user_id
    private int productId;      // cart_item.product_id
    private String name;        // product.name
    private String description; // product.description
    private String imageUrl;    // product.image_url (as String)
    private double price;       // product.price
    private int quantity;       // cart_item.quantity

    private String addedAt;     // cart_item.added_at

    public CartItem(int id, int userId, int productId, String name, String description, String imageUrl, double price, int quantity, String addedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
        this.addedAt = addedAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getAddedAt() { return addedAt; }

    public void setAddedAt(String addedAt) { this.addedAt = addedAt; }
}

