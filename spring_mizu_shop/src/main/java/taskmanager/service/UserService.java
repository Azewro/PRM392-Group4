package taskmanager.service;

import taskmanager.dto.ChangePasswordRequest;
import taskmanager.dto.UpdateUserProfileRequest;
import taskmanager.dto.UserDTO;
import taskmanager.dto.CreateUserRequest;
import taskmanager.dto.LoginRequest;
import taskmanager.dto.LoginResponse;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Integer id);
    UserDTO createUser(CreateUserRequest request);
    void deactivateUser(Integer id);

    UserDTO updateUserProfile(Integer id, UpdateUserProfileRequest request);
    void changePassword(Integer id, ChangePasswordRequest request);
    LoginResponse login(LoginRequest request);
}

