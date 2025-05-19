package group.assignment.booking_hotel_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("scheduled-task-");
        scheduler.setErrorHandler(throwable ->
                System.err.println("Scheduled task error: " + throwable.getMessage())
        );
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }
}