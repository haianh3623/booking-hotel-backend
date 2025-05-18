package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.models.DeviceToken;
import java.util.List;

public interface DeviceTokenService {
    DeviceToken saveOrUpdateToken(Integer userId, String token, String deviceType);
    List<DeviceToken> getActiveTokensByUserId(Integer userId);
    void deactivateToken(Integer userId, String token);
    void deactivateAllUserTokens(Integer userId);
    boolean isTokenExists(String token);
    
    // New method for removing invalid tokens
    void removeInvalidToken(String token);
    
    // Batch cleanup method
    void cleanupInvalidTokens(List<String> invalidTokens);
}