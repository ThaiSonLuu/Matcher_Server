package com.sanryoo.toifa.service;

import com.sanryoo.toifa.modal.Information;
import com.sanryoo.toifa.repository.InformationRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageStorageService implements IStorageService {

    @Autowired
    private InformationRepository informationRepository;

    private final Path storageFolder = Paths.get("images");

    public ImageStorageService() {
        try {
            Files.createDirectories(storageFolder);
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize storage", e);
        }
    }

    private Boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        assert fileExtension != null;
        return Arrays.asList(new String[]{"png", "jpg", "jpeg", "bmp"}).contains(fileExtension.trim().toLowerCase());
    }

    @Override
    public String uploadFile(Long id, String username, String column, MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file");
        }
        if (!isImageFile(file)) {
            throw new RuntimeException("You can only upload image file");
        }
        float fileSizeInMB = file.getSize() / 1_000_000.0f;
        if (fileSizeInMB > 5) {
            throw new RuntimeException("File must be <= 5MB");
        }
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = column + "_" + UUID.randomUUID().toString().replace("-", "") + "." + fileExtension;

        try {
            Files.createDirectories(Paths.get("images/" + username));
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize user's storage", e);
        }

        Path destinationFilePath = this.storageFolder.resolve(Paths.get(username + "/" + fileName))
                .normalize().toAbsolutePath();

        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            String BASE_URL = "http://10.244.146.254:8080/file/";
            String urlOfImage = BASE_URL + username + "/" + fileName;
            Optional<Information> foundInformation = informationRepository.findById(id);
            foundInformation.map(information -> {
                switch (column) {
                    case "avatar" -> information.setAvatar(urlOfImage);
                    case "image1" -> information.setImage1(urlOfImage);
                    case "image2" -> information.setImage2(urlOfImage);
                    case "image3" -> information.setImage3(urlOfImage);
                }
                return informationRepository.save(information);
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file");
        }
        return fileName;
    }

    @Override
    public byte[] readFileContent(String username, String fileName) {
        try {
            Path file = storageFolder.resolve(username + "/" + fileName);
            UrlResource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return StreamUtils.copyToByteArray(resource.getInputStream());
            } else {
                throw new RuntimeException(
                        "Could not read file: " + fileName);
            }
        } catch (IOException exception) {
            throw new RuntimeException("Could not read file: " + fileName, exception);
        }
    }
}
