package com.sanryoo.matcher.controllers;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.sanryoo.matcher.file_service.IStorageService;
import com.sanryoo.matcher.modal.MatcherMessage;
import com.sanryoo.matcher.modal.ResponseObject;
import com.sanryoo.matcher.modal.User;
import com.sanryoo.matcher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("file")
public class FileController {

    private final UserRepository userRepository;

    private final IStorageService storageService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/profile")
    ResponseEntity<ResponseObject> uploadFile(
            @RequestParam("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("column") String column,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            storageService.uploadImageProfile(id, username, column, file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(200, "ok", "Upload file successful", userRepository.findById(id).get())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(400, "failed", e.getMessage(), new User())
            );
        }
    }

    @PostMapping("/message")
    public void uploadImageMessage(
            @RequestParam("idsend") Long idsend,
            @RequestParam("idreceive") Long idreceive,
            @RequestParam("file") MultipartFile file
    ) {
        User usersend = userRepository.findById(idsend).get();
        User userreceive = userRepository.findById(idreceive).get();
        Date date = new Date(System.currentTimeMillis());
        MatcherMessage message = new MatcherMessage(null, usersend, userreceive, date, 1, "", 0, 0);

        MatcherMessage result = storageService.uploadImageMessage(message, file);

        simpMessagingTemplate.convertAndSend("/response/message/" + result.getUsersend().getId(), result);
        simpMessagingTemplate.convertAndSend("/response/message/" + result.getUserreceive().getId(), result);

        String fcmtokenreceive = userreceive.getFcmtoken();

        if (!fcmtokenreceive.isEmpty()) {
            Notification notification = Notification.builder()
                    .setTitle(result.getUsersend().getFullname())
                    .setBody("Sent an image")
                    .setImage(result.getData())
                    .build();
            Message message1 = Message.builder()
                    .setNotification(notification)
                    .setToken(fcmtokenreceive)
                    .build();
            try {
                FirebaseMessaging.getInstance().send(message1);
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @GetMapping("/load/{folder}/{subfolder}/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable("folder") String folder, @PathVariable("subfolder") String subfolder, @PathVariable("fileName") String fileName) {
        try {
            String path = folder + "/" + subfolder + "/" + fileName;
            byte[] bytes = storageService.readFileContent(path);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        } catch (Exception exception) {
            return ResponseEntity.noContent().build();
        }
    }
}
