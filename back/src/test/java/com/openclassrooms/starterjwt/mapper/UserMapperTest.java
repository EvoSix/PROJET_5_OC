package com.openclassrooms.starterjwt.mapper;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;


public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    @DisplayName("Convert User to UserDto - Success")
    void toDto_ShouldConvertUserToUserDto() {
        // Arrange
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", false, null, null);

        // Act
        UserDto userDto = userMapper.toDto(user);

        // Assert
        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstName(), userDto.getFirstName());
    }
    @Test
    @DisplayName("Convert UserDto to User - Success")
    void toEntity_ShouldConvertUserDtoToUser() {
        // Arrange
        UserDto userDto = new UserDto(1L, "test@example.com", "Doe", "John", false, "null", null, null);

        // Act
        User user = userMapper.toEntity(userDto);

        // Assert
        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getFirstName(), user.getFirstName());
    }
}
