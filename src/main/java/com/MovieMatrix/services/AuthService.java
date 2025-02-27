package com.MovieMatrix.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.MovieMatrix.models.User;
import com.MovieMatrix.repositories.UserRepository;
import com.MovieMatrix.utils.JwtUtil;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return "Username already exists";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
        return "User registered successfully";
    }

    public String login(String email, String password) {
//        Optional<User> user = userRepository.findByUsername(username);
        Optional<User> user = userRepository.findByEmail(email);
        String name = user.get().getUsername();
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return JwtUtil.generateToken(email,name);
        }
        return "Invalid username or password";
    }
}
