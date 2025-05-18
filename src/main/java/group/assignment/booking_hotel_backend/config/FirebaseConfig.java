package group.assignment.booking_hotel_backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@Slf4j
public class FirebaseConfig {
    
    @Value("${firebase.service-account-key:firebase-service-account.json}")
    private String firebaseServiceAccountKey;
    
    @Value("${firebase.enabled:false}")
    private boolean firebaseEnabled;
    
    @Bean
    @ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = false)
    public FirebaseApp firebaseApp() throws IOException {
        log.info("Initializing Firebase App...");
        
        try {
            // Check if FirebaseApp is already initialized
            if (!FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.getInstance();
            }
            
            ClassPathResource resource = new ClassPathResource(firebaseServiceAccountKey);
            
            if (!resource.exists()) {
                log.warn("Firebase service account file not found: {}", firebaseServiceAccountKey);
                log.warn("Firebase features will be disabled. To enable, place the service account file in the classpath and set firebase.enabled=true");
                return null;
            }
            
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .build();
            
            FirebaseApp app = FirebaseApp.initializeApp(options);
            log.info("Firebase App initialized successfully");
            return app;
            
        } catch (Exception e) {
            log.error("Failed to initialize Firebase App: {}", e.getMessage());
            log.warn("Firebase features will be disabled");
            return null;
        }
    }
    
    @Bean
    @ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = false)
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        if (firebaseApp == null) {
            log.warn("FirebaseApp is null, FirebaseMessaging will not be available");
            return null;
        }
        log.info("Initializing Firebase Messaging...");
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}