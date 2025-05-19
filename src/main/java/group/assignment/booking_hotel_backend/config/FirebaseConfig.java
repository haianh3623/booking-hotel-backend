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
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
        log.info("Initializing Firebase App... ");
        
        try {
            // Check if FirebaseApp is already initialized
            if (!FirebaseApp.getApps().isEmpty()) {
                log.info("Firebase App already initialized");
                return FirebaseApp.getInstance();
            }
            
            // Try to load from classpath first
            Resource resource = new ClassPathResource(firebaseServiceAccountKey);
            InputStream serviceAccountStream;
            
            if (resource.exists()) {
                serviceAccountStream = resource.getInputStream();
                log.info("Loading Firebase credentials from classpath: {}", firebaseServiceAccountKey);
            } else {
                // Try loading from environment variable
                String googleCredentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
                if (googleCredentials != null) {
                    log.info("Loading Firebase credentials from environment variable");
                    serviceAccountStream = new ClassPathResource(googleCredentials).getInputStream();
                } else {
                    log.warn("Firebase service account file not found: {}", firebaseServiceAccountKey);
                    log.warn("Firebase features will be disabled. To enable:");
                    log.warn("1. Place the service account file in src/main/resources/");
                    log.warn("2. Set firebase.enabled=true in application.yml");
                    log.warn("3. Or set GOOGLE_APPLICATION_CREDENTIALS environment variable");
                    return null;
                }
            }
            
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);
            
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();
            
            FirebaseApp app = FirebaseApp.initializeApp(options);
            log.info("Firebase App initialized successfully");
            return app;
            
        } catch (Exception e) {
            log.error("Failed to initialize Firebase App: {}", e.getMessage(), e);
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
        
        try {
            log.info("Initializing Firebase Messaging...");
            FirebaseMessaging messaging = FirebaseMessaging.getInstance(firebaseApp);
            log.info("Firebase Messaging initialized successfully");
            return messaging;
        } catch (Exception e) {
            log.error("Failed to initialize Firebase Messaging: {}", e.getMessage(), e);
            return null;
        }
    }
}