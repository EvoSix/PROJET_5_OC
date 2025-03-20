package com.openclassrooms.starterjwt.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Doe");
        teacher.setFirstName("John");
    }
    @Test
    @DisplayName("Find all teachers - Success")
    void findAll_ShouldReturnListOfTeachers() {
        // Arrange
        List<Teacher> teachers = List.of(teacher);
        when(teacherRepository.findAll()).thenReturn(teachers);

        // Act
        List<Teacher> result = teacherService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getLastName());
        verify(teacherRepository, times(1)).findAll();
    }
    @Test
    @DisplayName("Find teacher by ID - Success")
    void findById_ShouldReturnTeacher_WhenExists() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Act
        Teacher result = teacherService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(teacher.getId(), result.getId());
        assertEquals("Doe", result.getLastName());
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find teacher by ID - Not Found")
    void findById_ShouldReturnNull_WhenNotExists() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Teacher result = teacherService.findById(1L);

        // Assert
        assertNull(result);
        verify(teacherRepository, times(1)).findById(1L);
    }
}
