package com.movieMatrix.services;

import java.util.List;
import java.util.Optional;

import com.movieMatrix.config.SecurityConfig;
import com.movieMatrix.dtos.ChangePasswordRequest;
import com.movieMatrix.dtos.CommonResponse;
import com.movieMatrix.dtos.RegisterRequestDTO;
import com.movieMatrix.dtos.UserDTO;
import com.movieMatrix.exceptions.UserException;
import com.movieMatrix.models.User;
import com.movieMatrix.repositories.UserRepository;
import com.movieMatrix.utils.JwtUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Service
public class UserService {
    private static final String USER_NOT_FOUND = "User not found with ID: %d";
    public static final String BEARER_ = "Bearer ";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private SecurityConfig securityConfig;

    @Cacheable(value = "users", key = "'all'")
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
//        log.debug("Retrieved {} users from database", users.size());
        return users.stream()
                .map(UserDTO::fromUser)
                .toList();
    }

    @Cacheable(value = "users", key = "#id")
    public User getUserById(@NotNull Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException(String.format(USER_NOT_FOUND, id)));
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void deleteUserById(@NotNull Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserException(String.format(USER_NOT_FOUND, id));
        }
        userRepository.deleteById(id);
//        log.info("User deleted successfully with ID: {}", id);
    }

    @Transactional
    @CacheEvict(value = "users", key = "'all'")
    public UserDTO updateUser(@Valid RegisterRequestDTO request) {
//        log.debug("Updating user with email: {}", request.getEmail());
        User user = getUserById(Long.valueOf(request.getId()));
        updateUserFields(user, request);
        User savedUser = userRepository.save(user);
//        log.info("User updated successfully: {}", user.getEmail());
        return UserDTO.fromUser(savedUser);
    }

    private void updateUserFields(User user, RegisterRequestDTO request) {
        Optional.ofNullable(request.getName()).ifPresent(user::setName);
        Optional.ofNullable(request.getRole()).ifPresent(user::setRole);
        Optional.ofNullable(request.getPhoneNumber()).ifPresent(user::setPhoneNumber);
        Optional.ofNullable(request.getPassword()).ifPresent(user::setPassword);
        Optional.ofNullable(request.getProfilePicture()).ifPresent(user::setProfilePicture);
    }

    public void updateProfilePicture(Long id, String fileName) {
        // TODO Auto-generated method stub

    }

    public CommonResponse changePassword(String token, ChangePasswordRequest request) {
        try {
            String email = jwtUtil.extractUserEmail(token.replace(BEARER_, ""));
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                // Check if old password matches
                if (!securityConfig.passwordEncoder().matches(request.getOldPassword(), user.getPassword())) {
                    throw new UserException("Old password is incorrect!");
                }
                // Encrypt and save new password
                user.setPassword(securityConfig.passwordEncoder().encode(request.getNewPassword()));
                userRepository.save(user);

                return new CommonResponse("Password changed successfully!");
            }
            throw new UserException("User not found!");
        } catch (Exception e) {
            throw new UserException("Error changing password: ", e);
        }
    }
}
