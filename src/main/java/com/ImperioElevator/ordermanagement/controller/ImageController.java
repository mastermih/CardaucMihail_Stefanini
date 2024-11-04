package com.ImperioElevator.ordermanagement.controller;


import com.ImperioElevator.ordermanagement.security.JwtService;
import com.ImperioElevator.ordermanagement.service.UserSevice;
import com.ImperioElevator.ordermanagement.service.serviceimpl.CdnService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

@RestController
public class ImageController {


    private static final String UPLOAD_DIRECTORY = "public/userProfileImages";
    private  final UserSevice userService;
    private final CdnService cdnService;
    private final JwtService jwtService;

    public ImageController(UserSevice userSevice, CdnService cdnService, JwtService jwtService){
        this.userService = userSevice;
        this.cdnService = cdnService;
        this.jwtService = jwtService;
    }

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image,
                                              @RequestParam("userId") Long userId,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("WAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa  " + authorizationHeader);
        logger.debug("waaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa: " + authorizationHeader);
        System.out.println("WAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa  " + authorizationHeader);

        try {
            if (image.isEmpty() || !isImage(image)) {
                return new ResponseEntity<>("Please upload a valid image file.", HttpStatus.BAD_REQUEST);
            }
            String jwtToken = authorizationHeader.replace("Bearer ", "");
            String curentImagePath = userService.getUserImage(userId);
            logger.debug("waaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa: " + authorizationHeader);
            System.out.println("WAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa  " + authorizationHeader);


            if (authorizationHeader == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing");
            }

            if(curentImagePath != null){
                deleteExistingImage(curentImagePath);
            }

            String cdnUrl = cdnService.sendImageToCDN(image, userId, jwtToken);
            if (cdnUrl == null) {
                return new ResponseEntity<>("Failed to upload image to CDN", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            userService.addImageForUSer(userId, cdnUrl);
            return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
        } catch (IOException | SQLException e) {
            return new ResponseEntity<>("Error occurred while uploading image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType.equals("image/jpeg") || contentType.equals("image/png");
    }

    private void deleteExistingImage(String imagePath) throws IOException {
        Path fullPath = null;
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