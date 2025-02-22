package com.ImperioElevator.ordermanagement.controller;


import com.ImperioElevator.ordermanagement.service.UserSevice;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

@RestController
public class ImageController {


    private static final String UPLOAD_DIRECTORY = "public/userProfileImages";  // Directory to store the images
    private  final UserSevice userService;
    public ImageController(UserSevice userSevice){
        this.userService = userSevice;
    }
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image, @RequestParam("userId") Long userId) {
        try {
            // Validate if file is an image
            if (image.isEmpty() || !isImage(image)) {
                return new ResponseEntity<>("Please upload a valid image file.", HttpStatus.BAD_REQUEST);
            }

            String curentImagePath = userService.getUserImage(userId);

            if(curentImagePath != null){
                deleteExistingImage(curentImagePath);
            }


            // Create a directory if it doesn't exist
            File uploadDir = new File(UPLOAD_DIRECTORY);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Create a unique file name
            String fileName = userId + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIRECTORY, fileName);

            // Save the image on server
            Files.write(filePath, image.getBytes());
            String relativePath = "userProfileImages/" + fileName;

            userService.addImageForUSer(userId, relativePath);
            return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
        } catch (IOException | SQLException e) {
            return new ResponseEntity<>("Error occurred while uploading image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isImage(MultipartFile file) {
        // Check for valid image content types (jpeg, png)
        String contentType = file.getContentType();
        return contentType.equals("image/jpeg") || contentType.equals("image/png");
    }

    private void deleteExistingImage(String imagePath) throws IOException {
        Path fullPath = null; //The problem was in the image path that is saved in the db
        if (imagePath.contains("/")) {
            String[] pathParts = imagePath.split("/");
            String fileName = pathParts[pathParts.length - 1];
            fullPath = Paths.get(UPLOAD_DIRECTORY, fileName).normalize();
        } else {
            logger.error("Something went wrong with image remove: ", fullPath);
        }
        File imageFile = fullPath.toFile();

          if(imageFile.exists()){
              boolean deleted = imageFile.delete();
              if(!deleted){
                  throw new IOException("Failed to delete the old image");
              }
          }
    }
}