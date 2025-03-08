package com.MovieMatrix.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.MovieMatrix.dtos.RegisterRequest;
import com.MovieMatrix.models.Role;
import com.MovieMatrix.models.User;
import com.MovieMatrix.services.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;


    // ✅ Get All Users (for React Admin)
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ✅ Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUser(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // ✅ Update User (including role updates)
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody RegisterRequest request) {
        String response = userService.updateUser(request);
        return ResponseEntity.ok(response);
    }

    // ✅ Delete User by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
    
 // ✅ Add roles to a user
    @PutMapping("/add-roles/{userId}")
    public ResponseEntity<String> addRolesToUser(@PathVariable Long userId, @RequestBody List<Role> roles) {
        String response = userService.addRolesToUser(userId, roles);
        return ResponseEntity.ok(response);
    }

    // ✅ Remove roles from a user
    @PutMapping("/remove-roles/{userId}")
    public ResponseEntity<String> removeRolesFromUser(@PathVariable Long userId, @RequestBody List<Role> roles) {
        String response = userService.removeRolesFromUser(userId, roles);
        return ResponseEntity.ok(response);
    }

    // ✅ Update user roles entirely
    @PutMapping("/update-roles/{userId}")
    public ResponseEntity<String> updateUserRoles(@PathVariable Long userId, @RequestBody List<Role> roles) {
        String response = userService.updateUserRoles(userId, roles);
        return ResponseEntity.ok(response);
    }
}
