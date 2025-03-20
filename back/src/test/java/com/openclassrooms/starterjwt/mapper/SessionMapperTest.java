package com.openclassrooms.starterjwt.mapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private final SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
        session = new Session();
        session.setId(1L);
        session.setDescription("Test Session");

        sessionDto = new SessionDto();
        sessionDto.setDescription("Test Session");
    }

    @Test
    @DisplayName("Convert Session to SessionDto")
    void toDto_ShouldConvertSessionToDto() {
        // Arrange
        session.setUsers(List.of(new User(1L, "test@example.com", "Doe", "John", "password", false, null, null)));

        // Act
        SessionDto result = sessionMapper.toDto(session);

        // Assert
        assertNotNull(result);
        assertEquals(session.getDescription(), result.getDescription());
    }

    @Test
    @DisplayName("Convert SessionDto to Session")
    void toEntity_ShouldConvertSessionDtoToSession() {
        // Arrange
        when(teacherService.findById(anyLong())).thenReturn(new Teacher(1L, "Smith", "John", null, null));
        when(userService.findById(anyLong())).thenReturn(new User(1L, "test@example.com", "Doe", "John", "password", false, null, null));

        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(List.of(1L));

        // Act
        Session result = sessionMapper.toEntity(sessionDto);

        // Assert
        assertNotNull(result);
        assertEquals(sessionDto.getDescription(), result.getDescription());
        verify(teacherService, times(1)).findById(1L);
        verify(userService, times(1)).findById(1L);
    }
}
