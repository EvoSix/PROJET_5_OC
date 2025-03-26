package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Get User by ID, when exist")
    void findById_ShouldReturnUser_WhenExists() throws Exception {

        User user = new User(1L, "test@example.com", "Doe", "John", "password", false, null, null);
        UserDto userDto = new UserDto(1L, "test@example.com", "Doe", "John", false, "password", null, null);

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("Get User by ID, when not exist")
    void findById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get User by ID, when invalid")
    void findById_ShouldReturnBadRequest_WhenInvalidId() throws Exception {
        mockMvc.perform(get("/api/user/abc")) // ID non valide
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete User, when user Exist And Authorized")
    void delete_ShouldReturnOk_WhenUserExistsAndAuthorized() throws Exception {

        User user = new User(1L, "test@example.com", "Doe", "John", "password", false, null, null);
        UserDetails userDetails = mock(UserDetails.class);

        when(userService.findById(1L)).thenReturn(user);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null));

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Delete User, When User Not Same")
    void delete_ShouldReturnUnauthorized_WhenUserNotSame() throws Exception {

        User user = new User(1L, "other@example.com", "Doe", "John", "password", false, null, null);
        UserDetails userDetails = mock(UserDetails.class);

        when(userService.findById(1L)).thenReturn(user);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null));

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).delete(anyLong());
    }

    @Test
    @DisplayName("Delete User,when user not exist")
    void delete_ShouldReturnNotFound_WhenUserNotExists() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete User, when invalid")
    void delete_ShouldReturnBadRequest_WhenInvalidId() throws Exception {
        mockMvc.perform(delete("/api/user/abc")) // ID non valide
                .andExpect(status().isBadRequest());
    }
}
