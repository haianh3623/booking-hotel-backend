package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.models.DeviceToken;
import group.assignment.booking_hotel_backend.models.User;
import group.assignment.booking_hotel_backend.repository.DeviceTokenRepository;
import group.assignment.booking_hotel_backend.repository.UserRepository;
import group.assignment.booking_hotel_backend.services.DeviceTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceTokenServiceImpl implements DeviceTokenService {
    
    private final DeviceTokenRepository deviceTokenRepository;
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public DeviceToken saveOrUpdateToken(Integer userId, String token, String deviceType) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            
            Optional<DeviceToken> existingToken = deviceTokenRepository.findByDeviceToken(token);
            
            if (existingToken.isPresent()) {
                DeviceToken deviceToken = existingToken.get();
                deviceToken.setUser(user);
                deviceToken.setDeviceType(deviceType);
                deviceToken.setIsActive(true);
                return deviceTokenRepository.save(deviceToken);
            } else {
                DeviceToken newToken = DeviceToken.builder()
                        .deviceToken(token)
                        .deviceType(deviceType)
                        .isActive(true)
                        .user(user)
                        .build();
                return deviceTokenRepository.save(newToken);
            }
        } catch (Exception e) {
            log.error("Error saving device token for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to save device token", e);
        }
    }
    
    @Override
    public List<DeviceToken> getActiveTokensByUserId(Integer userId) {
        return deviceTokenRepository.findActiveTokensByUserId(userId);
    }
    
    @Override
    @Transactional
    public void deactivateToken(Integer userId, String token) {
        deviceTokenRepository.deactivateToken(userId, token);
    }
    
    @Override
    @Transactional
    public void deactivateAllUserTokens(Integer userId) {
        deviceTokenRepository.deactivateAllUserTokens(userId);
    }
    
    @Override
    public boolean isTokenExists(String token) {
        return deviceTokenRepository.findByDeviceToken(token).isPresent();
    }
    
    @Override
    @Transactional
    public void removeInvalidToken(String token) {
        try {
            Optional<DeviceToken> deviceToken = deviceTokenRepository.findByDeviceToken(token);
            if (deviceToken.isPresent()) {
                DeviceToken tokenEntity = deviceToken.get();
                tokenEntity.setIsActive(false);
                deviceTokenRepository.save(tokenEntity);
                log.info("Marked invalid token as inactive: {}", token);
            }
        } catch (Exception e) {
            log.error("Error removing invalid token {}: {}", token, e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void cleanupInvalidTokens(List<String> invalidTokens) {
        if (invalidTokens == null || invalidTokens.isEmpty()) {
            return;
        }
        
        try {
            for (String token : invalidTokens) {
                removeInvalidToken(token);
            }
            log.info("Cleaned up {} invalid tokens", invalidTokens.size());
        } catch (Exception e) {
            log.error("Error during batch cleanup of invalid tokens: {}", e.getMessage());
        }
    }
}