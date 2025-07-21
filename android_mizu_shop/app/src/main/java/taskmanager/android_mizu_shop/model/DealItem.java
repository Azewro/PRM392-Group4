package taskmanager.android_mizu_shop.model;

public class DealItem {
    private String imageUrl;
    private String name;
    private String price;

    public DealItem(String imageUrl, String name, String price) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
    }

    public String getImageUrl() { return imageUrl; }
    public String getName() { return name; }
    public String getPrice() { return price; }
}
