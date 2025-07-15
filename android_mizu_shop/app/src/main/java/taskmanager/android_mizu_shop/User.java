package taskmanager.android_mizu_shop;

public class User {
    private int id;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private String role;
    private boolean isActive;
    // Getter, Setter, Constructor
    public User(int id, String username, String email, String phone, String address, String avatar, String role, boolean isActive) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.role = role;
        this.isActive = isActive;
    }
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getAvatar() { return avatar; }
    public String getRole() { return role; }
    public boolean isActive() { return isActive; }
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setRole(String role) { this.role = role; }
    public void setActive(boolean active) { isActive = active; }
} 