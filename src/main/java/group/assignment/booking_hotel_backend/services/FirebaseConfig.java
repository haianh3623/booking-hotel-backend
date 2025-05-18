package group.assignment.booking_hotel_backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@Slf4j
public class FirebaseConfig {
    
    @Value("${firebase.service-account-key:firebase-service-account.json}")
    private String firebaseServiceAccountKey;
    
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        log.info("Initializing Firebase App...");
        
        try {
            // Check if FirebaseApp is already initialized
            if (!FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.getInstance();
            }
            
            ClassPathResource resource = new ClassPathResource(firebaseServiceAccountKey);
            
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .build();
            
            FirebaseApp app = FirebaseApp.initializeApp(options);
            log.info("Firebase App initialized successfully");
            return app;
            
        } catch (Exception e) {
            log.error("Failed to initialize Firebase App: {}", e.getMessage());
            throw e;
        }
    }
    
    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        log.info("Initializing Firebase Messaging...");
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}