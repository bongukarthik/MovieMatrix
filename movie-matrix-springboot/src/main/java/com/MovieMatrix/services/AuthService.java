package com.movieMatrix.services;

import com.movieMatrix.config.SecurityConfig;
import com.movieMatrix.dtos.CommonResponse;
import com.movieMatrix.dtos.LoginRequestDTO;
import com.movieMatrix.dtos.RegisterRequestDTO;
import com.movieMatrix.dtos.TokenResponse;
import com.movieMatrix.exceptions.UserException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.movieMatrix.models.Role;
import com.movieMatrix.models.User;
import com.movieMatrix.repositories.UserRepository;
import com.movieMatrix.utils.JwtUtil;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final SecurityConfig securityConfig;

    public CommonResponse register(RegisterRequestDTO request) {
        return Optional.ofNullable(request)
                .filter(r -> !userAlreadyExists(r))  // Only proceed if user does not already exist
                .map(r -> {
                    // Create and save the user if they do not exist
                    User user = createUserFromRequest(r);
                    userRepository.save(user);
                    // Log the successful registration with masked email
                    System.out.println("User successfully registered with email: " + maskEmail(r.getEmail()));
//                    return "User registered successfully";
                    return new CommonResponse("User registered successfully");
                }).orElseThrow(() -> new UserException("User already exists"));
    }


    private User createUserFromRequest(RegisterRequestDTO request) {
        return User.builder().name(request.getName()).email(request.getEmail())
                .password(getEncode(request))
                .phoneNumber(request.getPhoneNumber()).address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth()).role(getRole(request))
                .profilePicture(request.getProfilePicture()).status(true).registeredAt(LocalDateTime.now())
                .build();
    }

    private String getEncode(RegisterRequestDTO request) {
        return securityConfig.passwordEncoder().encode(request.getPassword());
    }

    private List<Role> getRole(RegisterRequestDTO request) {
        return Optional.ofNullable(request.getRole())
                .filter(roles -> !roles.isEmpty())
                .orElseGet(this::getDefaultRole);
    }

    // Get default role if no roles specified
    private List<Role> getDefaultRole() {
        return Collections.singletonList(
                Role.USER);
    }

    protected boolean userAlreadyExists(RegisterRequestDTO request) {
        System.out.println("userAlreadyExists: " + userRepository.existsByEmail(request.getEmail()));
        return userRepository.existsByEmail(request.getEmail());
    }

    @Transactional
    public TokenResponse login(LoginRequestDTO request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> isValidPassword(request.getPassword(), user))
                .map(this::authenticateUser)
                .orElseThrow(() -> new UserException("Please check your email and password"));
    }


    private boolean isValidPassword(String rawPassword, User user) {
        boolean isValid = securityConfig.passwordEncoder().matches(rawPassword, user.getPassword());
        if (!isValid) {
//            log.warn("Invalid password attempt for user: {}", maskEmail(user.getEmail()));
            System.out.println("Invalid password attempt for user: {}" + maskEmail(user.getEmail()));
        }
        return isValid;
    }

    private TokenResponse authenticateUser(User user) {
        if (!user.isStatus()) {
//            log.warn("Attempt to login to inactive account: {}", maskEmail(user.getEmail()));
            throw new UserException("Account is disabled");
//            System.out.println("Attempt to login to inactive account: {}" + maskEmail(user.getEmail()));
        }

        updateLastLogin(user);
//        String token = jwtUtil.generateToken(user);
        TokenResponse tokenResponse = generateTokens(user);

//        log.info("Successfully generated token for user: {}", maskEmail(user.getEmail()));
        System.out.println("Successfully generated token for user: {}" + maskEmail(user.getEmail()));
        return tokenResponse;
    }

    private TokenResponse generateTokens(User user) {
        String AccessToken = jwtUtil.generateToken(user);
        String RefreshToken = jwtUtil.generateRefreshToken(user);
        return new TokenResponse(AccessToken, RefreshToken);
    }

    private void updateLastLogin(User user) {
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }

    // Utility method to mask email for logging
    private String maskEmail(String email) {
        if (email == null || email.length() < 4) {
            return "****";
        }
        return email.substring(0, 2) + "***" + email.substring(email.lastIndexOf('@'));
    }

}
