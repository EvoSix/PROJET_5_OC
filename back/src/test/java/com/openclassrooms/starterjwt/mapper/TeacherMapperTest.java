package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

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
        Teacher teacher = new Teacher(1L, "Smith", "John", null, null);

        // Act
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Assert
        assertNotNull(teacherDto);
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
    }

    @Test
    @DisplayName("Convert TeacherDto to Teacher")
    void toEntity_ShouldConvertTeacherDtoToTeacher() {
        // Arrange
        TeacherDto teacherDto = new TeacherDto(1L, "Smith", "John",null, null);

        // Act
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Assert
        assertNotNull(teacher);
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
    }
}
