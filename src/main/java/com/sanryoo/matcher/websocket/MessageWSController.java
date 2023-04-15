package com.sanryoo.matcher.websocket;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.sanryoo.matcher.modal.MatcherMessage;
import com.sanryoo.matcher.modal.User;
import com.sanryoo.matcher.repository.MessageRepository;
import com.sanryoo.matcher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Controller
public class MessageWSController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final Path storageFolder = Paths.get("images");

    @MessageMapping("/message")
    public void send(MatcherMessage message) {
        Date date = new Date(System.currentTimeMillis());
        message.setDate(date);

        messageRepository.save(message);

        simpMessagingTemplate.convertAndSend("/response/message/" + message.getUsersend().getId(), message);
        simpMessagingTemplate.convertAndSend("/response/message/" + message.getUserreceive().getId(), message);

        User userReceive = userRepository.findById(message.getUserreceive().getId()).get();
        String fcmtokenreceive = userReceive.getFcmtoken();

        if (!fcmtokenreceive.isEmpty()) {
            Notification notification = Notification.builder()
                    .setTitle(message.getUsersend().getFullname())
                    .setBody(message.getData())
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
}
