package group.assignment.booking_hotel_backend.impl;

import group.assignment.booking_hotel_backend.dto.UserDto;
import group.assignment.booking_hotel_backend.mapper.UserMapper;
import group.assignment.booking_hotel_backend.models.User;
import group.assignment.booking_hotel_backend.repository.UserRepository;
import group.assignment.booking_hotel_backend.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void updateScoreByUserId_shouldUpdateScoreAndReturnDto() {
        // Arrange
        User user = new User();
        user.setUserId(1);
        user.setScore(2000);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        UserDto result = UserMapper.mapToUserDto(userService.updateScoreByUserId(1, 2000), new UserDto());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals(2000, result.getScore());
        verify(userRepository).findById(1);
        verify(userRepository).save(user);
    }

    @Test
    void updateScoreByUserId_shouldThrowIfUserNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.updateScoreByUserId(99, 2000));

        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void save_shouldCallRepositoryAndReturnSavedUser() {
        // Arrange
        User user = User.builder()
                .userId(1)
                .username("testuser")
                .score(100)
                .build();

        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.save(user);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals("testuser", result.getUsername());
        verify(userRepository).save(user);
    }
}
