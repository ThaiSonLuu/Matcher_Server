package com.sanryoo.matcher.fcm_service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Configuration
public class FCMInitializer {

    @Bean
    public FirebaseApp initializeFCM() throws IOException {
        String rootPath = Paths.get(".").toAbsolutePath().normalize().toString();
        FileInputStream serviceAccount = new FileInputStream(rootPath + "/src/main/resources/matcher_firebase.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        return FirebaseApp.initializeApp(options);
    }

}
