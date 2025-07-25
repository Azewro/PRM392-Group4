package taskmanager.android_mizu_shop.model;

public class CreateUserRequest {
    private String username;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String avatar;

    public CreateUserRequest() {}

    public CreateUserRequest(String username, String email, String password, String phone, String address, String avatar) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
    }

    // Getter & Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
} 