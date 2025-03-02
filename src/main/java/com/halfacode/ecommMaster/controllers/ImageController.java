package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private CloudinaryService cloudinaryService;

   /* @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        // Convert MultipartFile to File
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);

        // Upload the file to Cloudinary
        return cloudinaryService.uploadImage(file);
    }*/
}
