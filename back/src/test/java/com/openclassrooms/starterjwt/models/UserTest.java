package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void testUserEntity() {
        User user = new User();
        user.equals(new User());
        user.hashCode();
        user.toString();
        assertNotNull(user.toString());
    }


    @Test
    void testUserEntityBuilder() {
        User user = new User();

        // Correction : Ajouter un email dans le Builder
        User builtUser = User.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .build();

        user.equals(builtUser);
        assertNotNull(user.toString());
    }
    @Test
    void testUserSetters() {
        User user = new User();


        user.setEmail("test@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("password123");


        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password123", user.getPassword());
    }
}
