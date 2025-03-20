package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        userDetailsServiceImpl = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    @DisplayName("loadUserByUsername")
    void whenUsernameFound_thenReturnUserDetailsImpl(){
        String username = "toto3@toto.com";
        User user = new User();
        user.setId(4L);
        user.setFirstName("toto");
        user.setLastName("toto");
        user.setEmail(username);
        user.setPassword("test!1234");
        user.setAdmin(false);
        user.setCreatedAt(LocalDateTime.parse("2023-08-29T18:57:17"));
        user.setUpdatedAt(LocalDateTime.parse("2023-08-29T18:57:18"));

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .build();

        // Mock a request UserDetails
        UserDetails reqUserDetails = userDetailsServiceImpl.loadUserByUsername(username);

        // Compare userDetails & request user details
        assertEquals(userDetails,reqUserDetails);

        verify(userRepository, times(1)).findByEmail(username);
    }

    @Test
    @DisplayName("loadUserByUsername method, UsernameNotFoundException")
    void whenUsernameNotFound_thenReturnException(){
        String wrong_username = "error@mail.com";
        when(userRepository.findByEmail(wrong_username)).thenThrow(UsernameNotFoundException.class);
        assertThrows(UsernameNotFoundException.class, () -> {userDetailsServiceImpl.loadUserByUsername(wrong_username);});
        verify(userRepository, times(1)).findByEmail(wrong_username);
    }
}