package com.dodoDev.api;

import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

public class MyPetSOSUtil {
    public static String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return new String(Base64.encode(tokenBytes));
    }

    public static String saveImage(MultipartFile image) throws IOException {
        String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";


        StringBuilder filenames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, image.getOriginalFilename());
        filenames.append(image.getOriginalFilename());
        Files.write(fileNameAndPath, image.getBytes());

        return "/uploads/" + filenames;
    }
}