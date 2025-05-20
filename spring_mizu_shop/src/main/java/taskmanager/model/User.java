package taskmanager.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private Integer id;

    private String username;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String avatar;

    @Column(nullable = false)
    private String role; // "admin", "customer"

    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

