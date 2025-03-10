package com.movieMatrix.services;

import java.util.List;
import java.util.Optional;
import com.movieMatrix.dtos.RegisterRequestDTO;
import com.movieMatrix.dtos.UserDTO;
import com.movieMatrix.exceptions.UserException;
import com.movieMatrix.models.User;
import com.movieMatrix.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.constraints.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Service
public class UserService {
    private static final String USER_NOT_FOUND = "User not found with ID: %d";
    @Autowired
    private UserRepository userRepository;

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
//        if (request.getPassword() != null) {
//            user.setPassword(passwordEncoder.encode(request.getPassword()));
//        }
    }

//    end of new implementation

//    public String updateUser(RegisterRequestDTO request) {
//        User user = getUser(request);
//        // User user = optionalUser;
//        if (request.getName() != null)
//            user.setName(request.getName());
//        // if (request.getEmail() != null)
//        // user.setEmail(request.getEmail());
//        // if (request.getPhoneNumber() != null)
//        // user.setPhoneNumber(request.getPhoneNumber());
//        // if (request.getPassword() != null)
//        // user.setPassword(request.getPassword()); // Ensure encoding
//        updateRole(request, user);
//
//        userRepository.save(user);
//        return "User updated successfully";
//    }

//    private Optional<User> findUserByEmailorPhoneNumber(RegisterRequestDTO request) {
//        Optional<User> optionalUser =
//                userRepository.findByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber());
//        return optionalUser;
//    }

//    public User getUser(RegisterRequestDTO request) {
//        Optional<User> optionalUser = findUserByEmailorPhoneNumber(request);
//        return optionalUser
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//    }

//    private void updateRole(RegisterRequestDTO request, User user) {
//        if (request.getRole() != null)
//            user.setRole(request.getRole());
//    }

//    public void deleteUser(RegisterRequestDTO request) {
//        User user = getUser(request);
//        userRepository.delete(user);
//    }


//    public void deleteUserById(Long id) {
//        userRepository.deleteById(id);
//    }

//    public List<User> getAllUsers() {
//        // userRepository.findAll();
//        List<User> usersList = userRepository.findAll();
//        System.out.println("Userlist size: " + usersList.size());
//        return usersList;
//    }


    // ✅ Add roles to an existing user +> not required
//    public String addRolesToUser(Long userId, List<Role> roles) {
//        User user =
//                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        // Add new roles to the user
//        user.getRole().addAll(roles); // Assuming no duplicate roles
//        userRepository.save(user);
//        return "Roles added successfully";
//    }

    // ✅ Remove roles from a user => not required
//    public String removeRolesFromUser(Long userId, List<Role> roles) {
//        User user =
//                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        user.getRole().removeAll(roles); // Remove the specified roles
//        userRepository.save(user);
//        return "Roles removed successfully";
//    }

    // ✅ Update user roles => not required
//    public String updateUserRoles(Long userId, List<Role> roles) {
//        User user =
//                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        user.setRole(roles); // Set new roles
//        userRepository.save(user);
//        return "User roles updated successfully";
//    }

//    public User updateUserProfile(Long id, RegisterRequestDTO updateRequest) {
//        User user = userRepository.findById(id).get();
//        user.setName(updateRequest.getName());
//        user.setEmail(updateRequest.getEmail());
////    user.setPassword(updateRequest.getPassword()); // Add password encryption here
//        user.setPhoneNumber(updateRequest.getPhoneNumber());
//        user.setAddress(updateRequest.getAddress());
//        user.setProfilePicture(updateRequest.getProfilePicture());
//        return userRepository.save(user);
//    }

    public void updateProfilePicture(Long id, String fileName) {
        // TODO Auto-generated method stub

    }

    public String changePassword(RegisterRequestDTO request) {
        User user = getUserById(Long.valueOf(request.getId()));
        user.setPassword(request.getPassword());
        userRepository.save(user);
        return "Password changed successfully";
    }
}
