package com.openclassrooms.starterjwt.services;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "test@example.com", "Doe", "John", "password", false, null, null);
    }



    @Test
    @DisplayName("Find user by ID - Success")
    void findById_ShouldReturnUser_WhenExists() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find user by ID - Not Found")
    void findById_ShouldReturnNull_WhenNotExists() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.findById(1L);

        assertNull(result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Delete user - Success")
    void delete_ShouldRemoveUser() {

        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
