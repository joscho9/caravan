package com.example.caravan_spring_project.controller;

import com.example.caravan_spring_project.service.ImageStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import java.util.*;

@RestController
@RequestMapping("/api/images")
public class ImageUploadController {

    private final ImageStorageService imageStorageService;

    public ImageUploadController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("files") MultipartFile[] files,
                                        @RequestParam(name="description", required = false ) String[] descriptions,
                                        @RequestParam("caravanId") UUID caravanId) {
        try{
            List<String> filePaths = new ArrayList<>();
            for (MultipartFile file : files) {
                String filePath = imageStorageService.saveImage(file, caravanId, null);
                filePaths.add(filePath);
            }
            return ResponseEntity.ok(Map.of("filePath", filePaths));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        }
    }
}