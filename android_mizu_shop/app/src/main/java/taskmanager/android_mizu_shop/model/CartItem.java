package taskmanager.android_mizu_shop.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private int id;
    private int userId;
    private int productId;
    private String name;
    private String description;
    private String imageUrl;
    private double price;
    private int quantity;
    private String addedAt;

    // Constructor
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

    // Parcelable constructor
    protected CartItem(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        productId = in.readInt();
        name = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        addedAt = in.readString();
    }

    // Write to parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(productId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeString(addedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Creator for Parcelable
    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

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
