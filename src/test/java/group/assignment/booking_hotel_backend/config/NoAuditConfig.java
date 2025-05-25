package group.assignment.booking_hotel_backend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;

@TestConfiguration
public class NoAuditConfig {

    @Bean
    @Primary
    public AuditorAware<String> auditorProvider() {
        return () -> null; // Không cần thông tin auditor trong test
    }
}
