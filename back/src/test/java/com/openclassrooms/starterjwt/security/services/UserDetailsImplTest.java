package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {

    @Test
    @DisplayName("Build and get basic fields")
    void buildUserDetailsAndAccessFields() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("securePassword")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("user@example.com", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("securePassword", user.getPassword());
        assertTrue(user.getAdmin());
    }

    @Test
    @DisplayName("Default account status flags should be true")
    void accountStatus_ShouldBeTrue() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    @DisplayName("Authorities should return empty set")
    void authorities_ShouldReturnEmptyCollection() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    @DisplayName("Equals should compare based on ID")
    void equals_ShouldReturnTrueForSameId() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user3 = UserDetailsImpl.builder().id(2L).build();

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    @DisplayName("Equals should return false for null or different type")
    void equals_ShouldHandleNullAndDifferentType() {
        UserDetailsImpl user = UserDetailsImpl.builder().id(1L).build();

        assertNotEquals(user, null);
        assertNotEquals(user, "not a user");
    }
}
