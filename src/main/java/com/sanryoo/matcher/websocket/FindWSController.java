package com.sanryoo.matcher.websocket;

import com.sanryoo.matcher.modal.MatchData;
import com.sanryoo.matcher.modal.User;
import com.sanryoo.matcher.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Controller
public class FindWSController {

    private final Logger logger = LoggerFactory.getLogger(FindWSController.class);

    @Autowired
    private UserRepository informationRepository;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final Random random = new Random();

    private final HashMap<String, Integer> listUsersMatched = new HashMap<>();

    @MessageMapping("/find")
    public void find(User user) {
        logger.info("User: " + user.getUsername() + " is finding ...");
        new Thread(() -> {
            int i = 1;
            while (i <= 20) {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (informationRepository.findById(user.getId()).get().getSearching() == 0) {
                    Thread.currentThread().interrupt();
                    return;
                }
                List<User> listUserFounded = informationRepository.find(user);
                listUserFounded.removeIf(information1 -> {
                    double distance = distFrom(
                            user.getLatitude(), user.getLongitude(),
                            information1.getLatitude(), information1.getLongitude()
                    );
                    return user.getDistance() < distance || information1.getDistance() < distance;
                });
                if (!listUserFounded.isEmpty()) {
                    int index = 0;
                    if (listUserFounded.size() > 1) {
                        index = random.nextInt(0, listUserFounded.size());
                    }
                    User other = listUserFounded.get(index);
                    //Set searching of both user is 0
                    user.setSearching(0);
                    other.setSearching(0);
                    informationRepository.save(user);
                    informationRepository.save(other);

                    //Send current distance for both
                    double currentDistance = distFrom(
                            user.getLatitude(), user.getLongitude(),
                            other.getLatitude(), other.getLongitude()
                    );
                    other.setCurrentdistance(currentDistance);
                    user.setCurrentdistance(currentDistance);

                    simpMessagingTemplate.convertAndSend("/response/find/" + user.getId(), other);
                    simpMessagingTemplate.convertAndSend("/response/find/" + other.getId(), user);

                    if (user.getId() < other.getId()) {
                        listUsersMatched.put(user.getId() + "_" + other.getId(), 1);
                    } else {
                        listUsersMatched.put(other.getId() + "_" + user.getId(), 1);
                        logger.info("Added " + other.getId() + "_" + user.getId() + " into hash map");
                    }
                    Thread.currentThread().interrupt();
                    return;
                }
                i++;
            }
            simpMessagingTemplate.convertAndSend("/response/find/" + user.getId(), new User());
        }).start();
    }

    @MessageMapping("/confirm")
    public void confirm(MatchData confirmData) {
        Integer data = listUsersMatched.get(confirmData.getIds());
        if (data + confirmData.getData() == 2) {
            listUsersMatched.replace(confirmData.getIds(), 2);
        } else {
            String[] listId = confirmData.getIds().split("_");
            if (data + confirmData.getData() <= 1) {
                simpMessagingTemplate.convertAndSend("/response/confirm/" + listId[0], "0");
                simpMessagingTemplate.convertAndSend("/response/confirm/" + listId[1], "0");
                logger.info("Cancel find for: " + listId[0] + " and " + listId[1]);
            } else {
                simpMessagingTemplate.convertAndSend("/response/confirm/" + listId[0], "1");
                simpMessagingTemplate.convertAndSend("/response/confirm/" + listId[1], "1");
                logger.info("Match for: " + listId[0] + " and " + listId[1]);
            }
            listUsersMatched.remove(confirmData.getIds());
        }
    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371; //kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
