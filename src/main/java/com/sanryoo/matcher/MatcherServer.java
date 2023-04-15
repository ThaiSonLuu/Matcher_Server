package com.sanryoo.matcher;

import com.sanryoo.matcher.websocket.WebSocketConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class MatcherServer {

    public MatcherServer() throws UnknownHostException {
        Logger logger = LoggerFactory.getLogger(MatcherServer.class);
        logger.info("");
        logger.info("Api url is: " + "http://" + InetAddress.getLocalHost().getHostAddress() + ":8088");
        logger.info("Ws url is: " + "ws://" + InetAddress.getLocalHost().getHostAddress() + ":8088/matcher/websocket");
        logger.info("");
    }

    public static void main(String[] args) {
        SpringApplication.run(MatcherServer.class, args);
    }

}
