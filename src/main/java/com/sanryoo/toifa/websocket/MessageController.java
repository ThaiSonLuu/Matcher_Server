package com.sanryoo.toifa.websocket;

import com.sanryoo.toifa.modal.Message;
import com.sanryoo.toifa.modal.User2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/hello")
    @SendTo("/topic/greeting")
    public Message hello(String user) throws Exception {
        Thread.sleep(1000);
        System.out.println(user + "vua gui");
        return new Message("Hello " + user);
    }

}
