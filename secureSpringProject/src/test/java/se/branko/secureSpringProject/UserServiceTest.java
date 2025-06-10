package se.branko.secureSpringProject;


import org.junit.jupiter.api.Test;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import se.branko.secureSpringProject.repository.UserRepository;
import se.branko.secureSpringProject.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class UserServiceTest {

    @Test
    void usernameExists_returnTrue_ifUserExists() {
        // Arrange
        UserRepository mockRepo = mock(UserRepository.class);
        PasswordEncoder mockEncoder = mock(PasswordEncoder.class);
        UserService service = new UserService(mockRepo, mockEncoder);

        when(mockRepo.existsByUsername("branko")).thenReturn(true);

        // Act
        boolean result = service.usernameExists("branko");

        // Assert
        assertTrue(result);
        verify(mockRepo, times(1)).existsByUsername("branko");
    }

    @Test
    void usernameExists_returnFalse_ifUserDoesNotExist() {
        // Arrange
        UserRepository mockRepo = mock(UserRepository.class);
        PasswordEncoder mockEncoder = mock(PasswordEncoder.class);
        UserService service = new UserService(mockRepo, mockEncoder);

        when(mockRepo.existsByUsername("nobody")).thenReturn(false);

        // Act
        boolean result = service.usernameExists("nobody");

        // Assert
        assertFalse(result);
        verify(mockRepo, times(1)).existsByUsername("nobody");
    }
}
