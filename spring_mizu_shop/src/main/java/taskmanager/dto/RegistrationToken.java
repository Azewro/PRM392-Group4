package taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationToken {
    private String token;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String avatar;
    private LocalDateTime expiresAt;
} 