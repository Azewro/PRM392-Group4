package taskmanager.android_mizu_shop.model;

public class MenuItem {
    public String name;
    public int iconResId; // nếu dùng drawable sẵn
    public String imageUrl; // nếu dùng ảnh từ API (base64 hoặc url)

    public MenuItem(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
