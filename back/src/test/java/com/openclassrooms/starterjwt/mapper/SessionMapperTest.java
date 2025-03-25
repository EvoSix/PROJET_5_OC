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
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

    private SessionMapper sessionMapper;
    private TeacherService teacherService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        teacherService = mock(TeacherService.class);
        userService = mock(UserService.class);

        sessionMapper = new SessionMapperImpl();
        sessionMapper.teacherService = teacherService;
        sessionMapper.userService = userService;
    }

    @Test
    @DisplayName("Convert SessionDto to Session - Success")
    void toEntity_ShouldMapDtoToEntity() {

        Long teacherId = 1L;
        Long userId = 2L;

        Teacher teacher = new Teacher(teacherId, "Smith", "John", null, null);
        User user = new User(userId, "test@example.com", "Doe", "Jane", "pass", false, null, null);

        when(teacherService.findById(teacherId)).thenReturn(teacher);
        when(userService.findById(userId)).thenReturn(user);

        SessionDto dto = new SessionDto();
        dto.setId(10L);
        dto.setName("Test Session");
        dto.setDate(new Date());
        dto.setDescription("Description here");
        dto.setTeacher_id(teacherId);
        dto.setUsers(List.of(userId));


        Session session = sessionMapper.toEntity(dto);


        assertNotNull(session);
        assertEquals("Test Session", session.getName());
        assertEquals("Description here", session.getDescription());
        assertEquals(teacherId, session.getTeacher().getId());
        assertEquals(1, session.getUsers().size());
        assertEquals(userId, session.getUsers().get(0).getId());
    }

    @Test
    @DisplayName("Convert Session to SessionDto - Success")
    void toDto_ShouldMapEntityToDto() {
        // Arrange
        Teacher teacher = new Teacher(1L, "Smith", "John", null, null);
        User user = new User(2L, "test@example.com", "Doe", "Jane", "pass", false, null, null);

        Session session = new Session();
        session.setId(10L);
        session.setName("Test Session");
        session.setDate(new Date());
        session.setDescription("Description here");
        session.setTeacher(teacher);
        session.setUsers(List.of(user));


        SessionDto dto = sessionMapper.toDto(session);


        assertNotNull(dto);
        assertEquals("Test Session", dto.getName());
        assertEquals("Description here", dto.getDescription());
        assertEquals(1L, dto.getTeacher_id());
        assertEquals(List.of(2L), dto.getUsers());
    }

    @Test
    @DisplayName("Convert list of SessionDto to list of Session")
    void toEntityList_ShouldMapSessionDtoListToEntityList() {
        // Arrange
        SessionDto dto1 = new SessionDto();
        dto1.setId(1L);
        dto1.setName("Session A");
        dto1.setDescription("Desc A");
        dto1.setUsers(List.of());
        dto1.setTeacher_id(1L);

        SessionDto dto2 = new SessionDto();
        dto2.setId(2L);
        dto2.setName("Session B");
        dto2.setDescription("Desc B");
        dto2.setUsers(List.of());
        dto2.setTeacher_id(2L);

        when(teacherService.findById(anyLong())).thenReturn(new Teacher());

        // Act
        List<Session> result = sessionMapper.toEntity(List.of(dto1, dto2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("Session A", result.get(0).getName());
        assertEquals("Session B", result.get(1).getName());
    }

    @Test
    @DisplayName("Convert list of Session to list of SessionDto")
    void toDtoList_ShouldMapSessionListToDtoList() {
        // Arrange
        Session s1 = new Session();
        s1.setId(1L);
        s1.setName("Session A");
        s1.setDescription("Desc A");
        s1.setTeacher(new Teacher(1L, "Smith", "John", null, null));
        s1.setUsers(List.of());

        Session s2 = new Session();
        s2.setId(2L);
        s2.setName("Session B");
        s2.setDescription("Desc B");
        s2.setTeacher(new Teacher(2L, "Doe", "Jane", null, null));
        s2.setUsers(List.of());

        // Act
        List<SessionDto> result = sessionMapper.toDto(List.of(s1, s2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("Session A", result.get(0).getName());
        assertEquals("Session B", result.get(1).getName());
    }

    @Test
    @DisplayName("toEntity should return null when input is null")
    void toEntity_ShouldReturnNull_WhenInputIsNull() {
        assertNull(sessionMapper.toEntity((SessionDto) null));
    }

    @Test
    @DisplayName("toDto should return null when input is null")
    void toDto_ShouldReturnNull_WhenInputIsNull() {
        assertNull(sessionMapper.toDto((Session) null));
    }
}
