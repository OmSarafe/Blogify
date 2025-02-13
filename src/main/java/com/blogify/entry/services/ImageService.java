package com.blogify.entry.services;


import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class ImageService {
    private static final String BASE_URL="http://localhost:8081/dev/api/Blogify";
    private static final String IMAGE_DIRECTORY="src/main/resources/static/images/posts";
    private static final String IMAGE_URL="images/posts/";

    @PostConstruct
    public void init(){
        Path directoryPath= Paths.get(IMAGE_DIRECTORY);
        if(Files.notExists(directoryPath)){
            try{
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValidImageExtension(String filename) {
        String[] validExtensions = {".jpg", ".jpeg", ".png", ".gif"};
        for (String ext : validExtensions) {
            if (filename.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    public String saveImage(MultipartFile image) throws IOException, NoSuchAlgorithmException {
        if (!isValidImageExtension(image.getOriginalFilename())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image type");
        }
        String uniqueFileName= generateUniqueFileName(image.getOriginalFilename());
        Path filePath=Paths.get(IMAGE_DIRECTORY,uniqueFileName);

        try {
            Files.write(filePath, image.getBytes());
        } catch (IOException e) {
            throw new IOException("Failed to save image to file system: " + e.getMessage(), e);
        }
        String fileUrl=BASE_URL+IMAGE_URL+uniqueFileName;
        return fileUrl;

    }

    private String generateUniqueFileName(String originalFileName) throws NoSuchAlgorithmException{
        String timestamp=String.valueOf(System.currentTimeMillis());
        MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] hash=digest.digest((originalFileName + timestamp).getBytes());
        return Base64.getUrlEncoder().encodeToString(hash)+getFileExtension(originalFileName);
    }

    private String getFileExtension(String filename){
        return filename.contains(".")? filename.substring(filename.lastIndexOf(".")):"";
    }
}
