package com.movieMatrix.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.movieMatrix.dtos.RegisterRequest;
import com.movieMatrix.models.Role;
import com.movieMatrix.models.User;
import com.movieMatrix.repositories.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public String updateUser(RegisterRequest request) {
    User user = getUser(request);
    // User user = optionalUser;
    if (request.getName() != null)
      user.setName(request.getName());
    // if (request.getEmail() != null)
    // user.setEmail(request.getEmail());
    // if (request.getPhoneNumber() != null)
    // user.setPhoneNumber(request.getPhoneNumber());
    // if (request.getPassword() != null)
    // user.setPassword(request.getPassword()); // Ensure encoding
    updateRole(request, user);

    userRepository.save(user);
    return "User updated successfully";
  }

  private Optional<User> findUserByEmailorPhoneNumber(RegisterRequest request) {
    Optional<User> optionalUser =
        userRepository.findByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber());
    return optionalUser;
  }

  public User getUser(RegisterRequest request) {
    Optional<User> optionalUser = findUserByEmailorPhoneNumber(request);
    return optionalUser
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }

  private void updateRole(RegisterRequest request, User user) {
    if (request.getRole() != null)
      user.setRole(request.getRole());
  }

  public void deleteUser(RegisterRequest request) {
    User user = getUser(request);
    userRepository.delete(user);
  }

  public User getUser(Long id) {
    return userRepository.findById(id).get();
  }

  public void deleteUserById(Long id) {
    userRepository.deleteById(id);
  }

  public List<User> getAllUsers() {
    // userRepository.findAll();
    List<User> usersList = userRepository.findAll();
    System.out.println("Userlist size: " + usersList.size());
    return usersList;
  }


  // ✅ Add roles to an existing user
  public String addRolesToUser(Long userId, List<Role> roles) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    // Add new roles to the user
    user.getRole().addAll(roles); // Assuming no duplicate roles
    userRepository.save(user);
    return "Roles added successfully";
  }

  // ✅ Remove roles from a user
  public String removeRolesFromUser(Long userId, List<Role> roles) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    user.getRole().removeAll(roles); // Remove the specified roles
    userRepository.save(user);
    return "Roles removed successfully";
  }

  // ✅ Update user roles
  public String updateUserRoles(Long userId, List<Role> roles) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    user.setRole(roles); // Set new roles
    userRepository.save(user);
    return "User roles updated successfully";
  }

  public User updateUserProfile(Long id, RegisterRequest updateRequest) {
    User user = userRepository.findById(id).get();
    user.setName(updateRequest.getName());
    user.setEmail(updateRequest.getEmail());
//    user.setPassword(updateRequest.getPassword()); // Add password encryption here
    user.setPhoneNumber(updateRequest.getPhoneNumber());
    user.setAddress(updateRequest.getAddress());
    user.setProfilePicture(updateRequest.getProfilePicture());
    return userRepository.save(user);
  }

  public void updateProfilePicture(Long id, String fileName) {
    // TODO Auto-generated method stub
    
  }

}
