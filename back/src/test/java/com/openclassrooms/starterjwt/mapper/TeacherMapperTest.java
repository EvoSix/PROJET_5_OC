package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        teacherMapper = Mappers.getMapper(TeacherMapper.class);
    }

    @Test
    @DisplayName("Convert Teacher to TeacherDto")
    void toDto_ShouldConvertTeacherToDto() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Act
        TeacherDto dto = teacherMapper.toDto(teacher);

        // Assert
        assertNotNull(dto);
        assertEquals(teacher.getId(), dto.getId());
        assertEquals(teacher.getFirstName(), dto.getFirstName());
        assertEquals(teacher.getLastName(), dto.getLastName());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    @DisplayName("Convert TeacherDto to Teacher")
    void toEntity_ShouldConvertTeacherDtoToTeacher() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        TeacherDto dto = new TeacherDto(1L, "Doe", "John", now, now);

        // Act
        Teacher teacher = teacherMapper.toEntity(dto);

        // Assert
        assertNotNull(teacher);
        assertEquals(dto.getId(), teacher.getId());
        assertEquals(dto.getFirstName(), teacher.getFirstName());
        assertEquals(dto.getLastName(), teacher.getLastName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    @DisplayName("Convert list of TeacherDto to list of Teacher")
    void toEntityList_ShouldConvertListOfDtoToEntities() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        TeacherDto dto1 = new TeacherDto(1L, "Doe", "John", now, now);
        TeacherDto dto2 = new TeacherDto(2L, "Smith", "Anna", now, now);

        // Act
        var result = teacherMapper.toEntity(List.of(dto1, dto2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("Smith", result.get(1).getLastName());
    }

    @Test
    @DisplayName("Convert list of Teacher to list of TeacherDto")
    void toDtoList_ShouldConvertListOfEntitiesToDtos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Teacher t1 = Teacher.builder().id(1L).firstName("John").lastName("Doe").createdAt(now).updatedAt(now).build();
        Teacher t2 = Teacher.builder().id(2L).firstName("Anna").lastName("Smith").createdAt(now).updatedAt(now).build();

        // Act
        var result = teacherMapper.toDto(List.of(t1, t2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("Smith", result.get(1).getLastName());
    }
    @Test
    @DisplayName("toEntity should return null when input is null")
    void toEntity_ShouldReturnNull_WhenInputIsNull() {
        assertNull(teacherMapper.toEntity((TeacherDto) null));
    }

    @Test
    @DisplayName("toDto should return null when input is null")
    void toDto_ShouldReturnNull_WhenInputIsNull() {
        assertNull(teacherMapper.toDto((Teacher) null));
    }

    @Test
    @DisplayName("toEntityList should return null when input is null")
    void toEntityList_ShouldReturnNull_WhenInputIsNull() {
        assertNull(teacherMapper.toEntity((List<TeacherDto>) null));
    }

    @Test
    @DisplayName("toDtoList should return null when input is null")
    void toDtoList_ShouldReturnNull_WhenInputIsNull() {
        assertNull(teacherMapper.toDto((List<Teacher>) null));
    }

}
