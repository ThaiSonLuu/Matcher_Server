package com.sanryoo.matcher.websocket;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.sanryoo.matcher.modal.User;
import com.sanryoo.matcher.modal.MatchData;
import com.sanryoo.matcher.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class CancelMatchWSController {

    private final Logger logger = LoggerFactory.getLogger(CancelMatchWSController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/cancel_match")
    public void cancelMatch(MatchData cancelMatchData) {
        String[] listId = cancelMatchData.getIds().split("_");

        User user1 = userRepository.findById(Long.parseLong(listId[0])).get();
        User user2 = userRepository.findById(Long.parseLong(listId[1])).get();

        user1.setMatched(0L);
        user2.setMatched(0L);

        user1.setCurrentdistance(0.0);
        user2.setCurrentdistance(0.0);

        if (cancelMatchData.getData() == 1) {
            if (cancelMatchData.getIdsend() == Long.parseLong(listId[0])) {
                user2.setBanned(user2.getBanned() + 1);
            } else {
                user1.setBanned(user1.getBanned() + 1);
            }
        }

        userRepository.save(user1);
        userRepository.save(user2);


        simpMessagingTemplate.convertAndSend("/response/cancel_match/" + listId[0], "1");
        simpMessagingTemplate.convertAndSend("/response/cancel_match/" + listId[1], "1");

        if (!Objects.equals(user1.getId(), cancelMatchData.getIdsend()) && !user1.getFcmtoken().isEmpty()) {
            Notification notification = Notification.builder()
                    .setTitle("Matcher")
                    .setBody("The match has been cancel")
                    .build();
            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(user1.getFcmtoken())
                    .build();
            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        }
        if (!Objects.equals(user2.getId(), cancelMatchData.getIdsend()) && !user2.getFcmtoken().isEmpty()) {
            Notification notification = Notification.builder()
                    .setTitle("Matcher")
                    .setBody("The match has been cancel")
                    .build();
            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(user2.getFcmtoken())
                    .build();
            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        }

        logger.info("Canceled match for " + listId[0] + " and " + listId[1]);
    }
}
