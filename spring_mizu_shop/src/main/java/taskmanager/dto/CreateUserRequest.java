package taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    @NotBlank(message = "Username không được để trống")
    private String username;
    @NotBlank(message = "Email không được để trống")
    private String email;
    @NotBlank(message = "Password không được để trống")
    private String password;
    @NotBlank(message = "Phone không được để trống")
    private String phone;
    @NotBlank(message = "Address không được để trống")
    private String address;
    @NotBlank(message = "Avatar không được để trống")
    private String avatar;
}

