package com.MovieMatrix.controllers;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.MovieMatrix.dtos.RegisterRequest;
import com.MovieMatrix.models.User;
import com.MovieMatrix.services.FileStorageService;
import com.MovieMatrix.services.UserService;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileStorageService fileStorageService;

    // Fetch Profile Data
    @GetMapping("/get")
    public ResponseEntity<User> getUserProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userService.getUser(((User) userPrincipal).getId()); // Get the logged-in user
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUser(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // Update Profile Data
//    @PutMapping("/update")
//    public ResponseEntity<User> updateProfile(@AuthenticationPrincipal UserPrincipal userPrincipal, 
//                                               @RequestBody RegisterRequest updateRequest) {
//        User updatedUser = userService.updateUserProfile(((User) userPrincipal).getId(), updateRequest);
//        return ResponseEntity.ok(updatedUser);
//    }
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody RegisterRequest request) {
        String response = userService.updateUser(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/uploadProfilePicture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {
        String fileName = fileStorageService.storeFile(file); // Your file storage service to handle the file
        userService.updateProfilePicture(((User) userPrincipal).getId(), fileName); // Update the profile picture in the DB
        return ResponseEntity.ok("Profile picture updated successfully");
    }
}
