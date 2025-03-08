package com.movieMatrix.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    // Define the directory where files will be stored
    private final Path uploadPath;

    // Constructor initializes the directory
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath); // Ensure the directory exists
        } catch (IOException e) {
            throw new RuntimeException("Could not create the upload directory!", e);
        }
    }

    // Store the uploaded file in the server's file system
    public String storeFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Copy file (overwrite if exists)
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName; // Return the stored file's name
    }

    // Retrieve the file path by its name
    public Path loadFileAsResource(String fileName) {
        return uploadPath.resolve(fileName);
    }

    // Delete a file
    public void deleteFile(String fileName) {
        Path filePath = uploadPath.resolve(fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + fileName, e);
        }
    }
}
