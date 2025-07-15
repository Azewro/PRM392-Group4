package taskmanager.android_mizu_shop;

public class UpdateUserProfileRequest {
    private String username;
    private String phone;
    private String address;
    private String avatar;
    private String role;
    private boolean isActive;

    public UpdateUserProfileRequest(String username, String phone, String address, String avatar, String role, boolean isActive) {
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.role = role;
        this.isActive = isActive;
    }
    // Getter & Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
} 