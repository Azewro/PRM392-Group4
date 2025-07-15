package taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taskmanager.dto.CreateUserRequest;
import taskmanager.dto.RegistrationToken;
import taskmanager.model.User;
import taskmanager.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RegistrationService {
    private final Map<String, RegistrationToken> tokenStore = new ConcurrentHashMap<>();
    private final UserRepository userRepository;

    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createRegistrationToken(CreateUserRequest request) {
        String token = UUID.randomUUID().toString();
        RegistrationToken regToken = new RegistrationToken(
                token,
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                request.getAddress(),
                request.getAvatar(),
                LocalDateTime.now().plusHours(1)
        );
        tokenStore.put(token, regToken);
        return token;
    }

    public boolean verifyTokenAndCreateUser(String token) {
        RegistrationToken regToken = tokenStore.get(token);
        if (regToken == null || regToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        // Kiểm tra trùng username/email
        if (userRepository.findByUsername(regToken.getUsername()) != null ||
            userRepository.findByEmail(regToken.getEmail()) != null) {
            tokenStore.remove(token);
            return false;
        }
        User user = User.builder()
                .username(regToken.getUsername())
                .email(regToken.getEmail())
                .password(regToken.getPassword())
                .phone(regToken.getPhone())
                .address(regToken.getAddress())
                .avatar(regToken.getAvatar())
                .role("customer")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        tokenStore.remove(token);
        return true;
    }
} 