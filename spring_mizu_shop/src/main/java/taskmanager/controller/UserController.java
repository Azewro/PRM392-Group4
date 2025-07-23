package taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanager.dto.*;
import taskmanager.service.UserService;
import taskmanager.dto.LoginRequest;
import taskmanager.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import taskmanager.dto.RegistrationToken;
import taskmanager.service.RegistrationService;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private JavaMailSender mailSender;

    // 🔹 GET ALL USERS
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users); // 200 OK
    }

    // 🔹 GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user); // 200 OK
    }

    // 🔹 CREATE NEW USER
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid CreateUserRequest request) {
        UserDTO createdUser = userService.createUser(request);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED); // 201 Created
    }

    // 🔹 UPDATE PROFILE
    @PutMapping("/{id}/profile")
    public ResponseEntity<UserDTO> updateProfile(@PathVariable Integer id, @RequestBody UpdateUserProfileRequest request) {
        UserDTO updatedUser = userService.updateUserProfile(id, request);
        return ResponseEntity.ok(updatedUser); // 200 OK
    }

    // 🔹 CHANGE PASSWORD
    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Integer id, @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // 🔹 DEACTIVATE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Integer id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // 🔹 LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 🔹 REGISTER (email verification)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid CreateUserRequest request) {
        // Kiểm tra trùng username/email
        if (userService.getAllUsers().stream().anyMatch(u -> u.getUsername().equals(request.getUsername()) || u.getEmail().equals(request.getEmail()))) {
            return ResponseEntity.badRequest().body("Username hoặc email đã tồn tại");
        }
        String token = registrationService.createRegistrationToken(request);
        // Gửi email xác thực
        String verifyLink = "http://localhost:8080/api/users/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Xác thực đăng ký tài khoản");
        message.setText("Nhấn vào link sau để xác thực tài khoản: " + verifyLink);
        mailSender.send(message);
        return ResponseEntity.ok("Đã gửi email xác thực. Vui lòng kiểm tra email để hoàn tất đăng ký.");
    }

    // 🔹 VERIFY EMAIL
    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        boolean ok = registrationService.verifyTokenAndCreateUser(token);
        if (ok) {
            return ResponseEntity.ok("Xác thực thành công! Bạn có thể đăng nhập.");
        } else {
            return ResponseEntity.badRequest().body("Token không hợp lệ hoặc đã hết hạn, hoặc username/email đã tồn tại.");
        }
    }
}

