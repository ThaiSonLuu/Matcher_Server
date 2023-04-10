package com.sanryoo.matcher.file_service;

import com.sanryoo.matcher.modal.MatcherMessage;
import com.sanryoo.matcher.modal.User;
import com.sanryoo.matcher.repository.MessageRepository;
import com.sanryoo.matcher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private Environment environment;

    private final Path storageFolder = Paths.get("images");
    private final Path storageProfile = storageFolder.resolve("profile");
    private final Path storageMessage = storageFolder.resolve("message");
    public ImageStorageService() {
        try {
            Files.createDirectories(storageFolder);
            Files.createDirectories(storageProfile);
            Files.createDirectories(storageMessage);
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize storage", e);
        }
    }

    public String getServerAddress() throws UnknownHostException {
        String serverPort = environment.getProperty("server.port");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        return "http://" + hostAddress + ":" + serverPort;
    }

    private Boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        assert fileExtension != null;
        return Arrays.asList(new String[]{"png", "jpg", "jpeg", "bmp"}).contains(fileExtension.trim().toLowerCase());
    }

    @Override
    public void uploadImageProfile(Long id, String username, String column, MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file");
        }
        if (!isImageFile(file)) {
            throw new RuntimeException("You can only upload image file");
        }
        float fileSizeInMB = file.getSize() / 1_000_000.0f;
        if (fileSizeInMB > 10) {
            throw new RuntimeException("File must be <= 10MB");
        }
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = column + "_" + UUID.randomUUID().toString().replace("-", "") + "." + fileExtension;

        try {
            Files.createDirectories(storageProfile.resolve(username));
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize user's storage", e);
        }

        Path destinationFilePath = storageProfile.resolve(Paths.get(username + "/" + fileName))
                .normalize().toAbsolutePath();
        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
//            String BASE_URL = "http://172.20.10.4:8080/file/load/profile/";
            String BASE_URL = getServerAddress() + "/file/load/profile";
            String urlOfImage = BASE_URL + "/" + username + "/" + fileName;
            Optional<User> foundInformation = userRepository.findById(id);
            foundInformation.map(i -> {
                switch (column) {
                    case "avatar" -> i.setAvatar(urlOfImage);
                    case "image1" -> i.setImage1(urlOfImage);
                    case "image2" -> i.setImage2(urlOfImage);
                    case "image3" -> i.setImage3(urlOfImage);
                }
                return userRepository.save(i);
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file");
        }
    }

    @Override
    public MatcherMessage uploadImageMessage(MatcherMessage message, MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file");
        }
        if (!isImageFile(file)) {
            throw new RuntimeException("You can only upload image file");
        }
        float fileSizeInMB = file.getSize() / 1_000_000.0f;
        if (fileSizeInMB > 10) {
            throw new RuntimeException("File must be <= 10MB");
        }
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + fileExtension;

        String subfolder;
        try {
            Long idsend = message.getUsersend().getId();
            Long idreceive = message.getUserreceive().getId();
            if (idsend < idreceive) {
                subfolder = idsend + "_" + idreceive;
            } else {
                subfolder = idreceive + "_" + idsend;
            }
            Files.createDirectories(storageMessage.resolve(subfolder));
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize user's storage", e);
        }

        Path destinationFilePath = storageMessage.resolve(Paths.get(subfolder + "/" + fileName))
                .normalize().toAbsolutePath();
        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
//            String BASE_URL = "http://172.20.10.4:8080/file/load/message";
            String BASE_URL = getServerAddress() + "/file/load/message";
            String urlOfImage = BASE_URL + "/" + subfolder + "/" + fileName;
            message.setData(urlOfImage);

            byte[] bytes = file.getBytes();
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            message.setWidth(width);
            message.setHeight(height);

            return messageRepository.save(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file");
        }
    }

    @Override
    public byte[] readFileContent(String path) {
        try {
            Path file = storageFolder.resolve(path);
            UrlResource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return StreamUtils.copyToByteArray(resource.getInputStream());
            } else {
                throw new RuntimeException("Could not read file: ");
            }
        } catch (IOException exception) {
            throw new RuntimeException("Could not read file: ", exception);
        }
    }
}
