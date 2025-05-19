package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.DeviceTokenRequest;
import group.assignment.booking_hotel_backend.models.DeviceToken;
import group.assignment.booking_hotel_backend.services.DeviceTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/device-token")
@RequiredArgsConstructor
@Slf4j
public class DeviceTokenController {
    
    private final DeviceTokenService deviceTokenService;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerDeviceToken(@RequestBody DeviceTokenRequest request) {
        try {
            DeviceToken savedToken = deviceTokenService.saveOrUpdateToken(
                    request.getUserId(),
                    request.getDeviceToken(),
                    request.getDeviceType()
            );
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Device token registered successfully",
                    "tokenId", savedToken.getId()
            ));
            
        } catch (Exception e) {
            log.error("Error registering device token: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Failed to register device token: " + e.getMessage()
            ));
        }
    }
    
    @DeleteMapping("/unregister")
    public ResponseEntity<Map<String, Object>> unregisterDeviceToken(
            @RequestParam Integer userId,
            @RequestParam String deviceToken) {
        try {
            deviceTokenService.deactivateToken(userId, deviceToken);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Device token unregistered successfully"
            ));
            
        } catch (Exception e) {
            log.error("Error unregistering device token: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Failed to unregister device token: " + e.getMessage()
            ));
        }
    }
    
    @DeleteMapping("/unregister-all/{userId}")
    public ResponseEntity<Map<String, Object>> unregisterAllUserTokens(@PathVariable Integer userId) {
        try {
            deviceTokenService.deactivateAllUserTokens(userId);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "All device tokens unregistered successfully"
            ));
            
        } catch (Exception e) {
            log.error("Error unregistering all device tokens: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Failed to unregister all device tokens: " + e.getMessage()
            ));
        }
    }
}