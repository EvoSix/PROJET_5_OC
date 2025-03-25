package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    @DisplayName("Convert UserDto to User")
    void toEntity_ShouldMapDtoToEntity() {
        LocalDateTime now = LocalDateTime.now();
        UserDto dto = new UserDto(
                1L,
                "john.doe@example.com",
                "Doe",
                "John",
                true,
                "securePass123",
                now,
                now
        );

        User user = userMapper.toEntity(dto);

        assertNotNull(user);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getLastName(), user.getLastName());
        assertEquals(dto.getFirstName(), user.getFirstName());
        assertEquals(dto.getPassword(), user.getPassword());
        assertEquals(dto.isAdmin(), user.isAdmin());
        assertEquals(dto.getCreatedAt(), user.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), user.getUpdatedAt());
    }

    @Test
    @DisplayName("Convert User to UserDto")
    void toDto_ShouldMapEntityToDto() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(
                1L,
                "john.doe@example.com",
                "Doe",
                "John",
                "securePass123",
                true,
                now,
                now
        );

        UserDto dto = userMapper.toDto(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getPassword(), dto.getPassword());
        assertEquals(user.isAdmin(), dto.isAdmin());
        assertEquals(user.getCreatedAt(), dto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    @DisplayName("Convert null User to null UserDto")
    void toDto_ShouldReturnNull_WhenInputIsNull() {
        assertNull(userMapper.toDto((User) null));
    }

    @Test
    @DisplayName("Convert null UserDto to null User")
    void toEntity_ShouldReturnNull_WhenInputIsNull() {
        assertNull(userMapper.toEntity((UserDto) null));
    }

    @Test
    @DisplayName("Convert list of UserDto to list of User")
    void toEntityList_ShouldConvertListDtoToEntities() {
        LocalDateTime now = LocalDateTime.now();
        List<UserDto> dtoList = List.of(
                new UserDto(1L, "a@example.com", "Last1", "First1", false, "pass1", now, now),
                new UserDto(2L, "b@example.com", "Last2", "First2", true, "pass2", now, now)
        );

        List<User> users = userMapper.toEntity(dtoList);

        assertEquals(2, users.size());
        assertEquals("a@example.com", users.get(0).getEmail());
        assertEquals("b@example.com", users.get(1).getEmail());
    }

    @Test
    @DisplayName("Convert list of User to list of UserDto")
    void toDtoList_ShouldConvertListEntitiesToDto() {
        LocalDateTime now = LocalDateTime.now();
        List<User> users = List.of(
                new User(1L, "a@example.com", "Last1", "First1", "pass1", false, now, now),
                new User(2L, "b@example.com", "Last2", "First2", "pass2", true, now, now)
        );

        List<UserDto> dtos = userMapper.toDto(users);

        assertEquals(2, dtos.size());
        assertEquals("a@example.com", dtos.get(0).getEmail());
        assertEquals("b@example.com", dtos.get(1).getEmail());
    }

    @Test
    @DisplayName("Convert null list of UserDto to null")
    void toEntityList_ShouldReturnNull_WhenInputIsNull() {
        assertNull(userMapper.toEntity((List<UserDto>) null));
    }

    @Test
    @DisplayName("Convert null list of User to null")
    void toDtoList_ShouldReturnNull_WhenInputIsNull() {
        assertNull(userMapper.toDto((List<User>) null));
    }
}
