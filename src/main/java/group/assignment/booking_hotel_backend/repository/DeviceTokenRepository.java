package group.assignment.booking_hotel_backend.repository;

import group.assignment.booking_hotel_backend.models.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Integer> {
    
    @Query("SELECT dt FROM DeviceToken dt WHERE dt.user.userId = :userId AND dt.isActive = true")
    List<DeviceToken> findActiveTokensByUserId(@Param("userId") Integer userId);
    
    @Query("SELECT dt FROM DeviceToken dt WHERE dt.deviceToken = :token")
    Optional<DeviceToken> findByDeviceToken(@Param("token") String token);
    
    @Modifying
    @Query("UPDATE DeviceToken dt SET dt.isActive = false WHERE dt.user.userId = :userId AND dt.deviceToken = :token")
    void deactivateToken(@Param("userId") Integer userId, @Param("token") String token);
    
    @Modifying
    @Query("UPDATE DeviceToken dt SET dt.isActive = false WHERE dt.user.userId = :userId")
    void deactivateAllUserTokens(@Param("userId") Integer userId);
    
    @Query("SELECT COUNT(dt) FROM DeviceToken dt WHERE dt.user.userId = :userId AND dt.isActive = true")
    Long countActiveTokensByUserId(@Param("userId") Integer userId);
}