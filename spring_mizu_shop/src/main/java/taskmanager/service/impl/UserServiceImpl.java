package taskmanager.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taskmanager.dto.ChangePasswordRequest;
import taskmanager.dto.UpdateUserProfileRequest;
import taskmanager.dto.UserDTO;
import taskmanager.dto.CreateUserRequest;
import taskmanager.dto.LoginRequest;
import taskmanager.dto.LoginResponse;
import taskmanager.mapper.UserMapper;
import taskmanager.model.User;
import taskmanager.repository.UserRepository;
import taskmanager.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + id));
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword()) // thực tế nên mã hóa
                .phone(request.getPhone())
                .address(request.getAddress())
                .avatar(request.getAvatar())
                .role("customer")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void deactivateUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        user.setIsActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public UserDTO updateUserProfile(Integer id, UpdateUserProfileRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        user.setUsername(request.getUsername());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setAvatar(request.getAvatar());
        if (request.getRole() != null) user.setRole(request.getRole());
        if (request.getIsActive() != null) user.setIsActive(request.getIsActive());
        user.setUpdatedAt(LocalDateTime.now());

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void changePassword(Integer id, ChangePasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu mới không khớp");
        }

        user.setPassword(request.getNewPassword()); // nên hash nếu có security
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null || !user.getIsActive()) {
            throw new RuntimeException("Tài khoản không tồn tại hoặc đã bị khóa");
        }
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }
        // Nếu có JWT thì sinh token, ở đây trả về chuỗi giả lập
        String token = "dummy-token";
        return new LoginResponse(token, userMapper.toDTO(user));
    }
}
