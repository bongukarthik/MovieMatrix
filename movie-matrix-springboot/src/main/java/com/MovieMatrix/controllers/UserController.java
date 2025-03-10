package com.movieMatrix.controllers;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;

import com.movieMatrix.dtos.ChangePasswordRequest;
import com.movieMatrix.dtos.CommonResponse;
import com.movieMatrix.dtos.RegisterRequestDTO;
import com.movieMatrix.dtos.UserDTO;
import com.movieMatrix.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.movieMatrix.models.User;
import com.movieMatrix.services.UserService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileStorageService fileStorageService;

    //  Get All Users
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //  Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    //  Update User
    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody RegisterRequestDTO request) {
        UserDTO response = userService.updateUser(request);
        return ResponseEntity.ok(response);
    }

    // Delete User by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(new CommonResponse("User deleted successfully."));
    }

    //change password
    @PostMapping("/changePassword")
    public ResponseEntity<CommonResponse> changePassword(@RequestHeader("Authorization") String token, @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(userService.changePassword(token, request));
    }

    //Update or upload profile picture
    @PostMapping("/uploadProfilePicture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {
        String fileName = fileStorageService.storeFile(file); // Your file storage service to handle the file
        userService.updateProfilePicture(((User) userPrincipal).getId(), fileName); // Update the profile picture in the DB
        return ResponseEntity.ok("Profile picture updated successfully");
    }

    // ✅ Add roles to a user
//    @PutMapping("/add-roles/{userId}")
//    public ResponseEntity<String> addRolesToUser(@PathVariable Long userId, @RequestBody List<Role> roles) {
//        String response = userService.addRolesToUser(userId, roles);
//        return ResponseEntity.ok(response);
//    }

    // ✅ Remove roles from a user
//    @PutMapping("/remove-roles/{userId}")
//    public ResponseEntity<String> removeRolesFromUser(@PathVariable Long userId, @RequestBody List<Role> roles) {
//        String response = userService.removeRolesFromUser(userId, roles);
//        return ResponseEntity.ok(response);
//    }

    // ✅ Update user roles entirely
//    @PutMapping("/update-roles/{userId}")
//    public ResponseEntity<String> updateUserRoles(@PathVariable Long userId, @RequestBody List<Role> roles) {
//        String response = userService.updateUserRoles(userId, roles);
//        return ResponseEntity.ok(response);
//    }
}
