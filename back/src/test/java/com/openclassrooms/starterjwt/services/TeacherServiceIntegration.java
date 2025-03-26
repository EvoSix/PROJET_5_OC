package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)

@SpringBootTest
@ActiveProfiles("test")
public class TeacherServiceIntegration {
    @Autowired
    private TeacherRepository teacherRepository;

    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherRepository);
        teacherRepository.deleteAll(); // Nettoyer la base avant chaque test
    }

    @Test
    @DisplayName("Find all teachers - Success")
    void findAll_ShouldReturnListOfTeachers() {

        Teacher teacher1 = new Teacher(null, "Doe", "John", null, null);
        Teacher teacher2 = new Teacher(null, "Smith", "Jane", null, null);
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);

        List<Teacher> teachers = teacherService.findAll();

        assertEquals(2, teachers.size());
        assertEquals("Smith", teachers.get(0).getLastName());
        assertEquals("Doe", teachers.get(1).getLastName());
    }

    @Test
    @DisplayName("Find teacher by ID - Success")
    void findById_ShouldReturnTeacher_WhenExists() {

        Teacher teacher = new Teacher(null, "Doe", "John", null, null);
        Teacher savedTeacher = teacherRepository.save(teacher);

        Teacher foundTeacher = teacherService.findById(savedTeacher.getId());

        assertNotNull(foundTeacher);
        assertEquals(savedTeacher.getId(), foundTeacher.getId());
        assertEquals("Doe", foundTeacher.getLastName());
    }
    @Test
    @DisplayName("Find teacher by ID - Not Found")
    void findById_ShouldReturnNull_WhenNotExists() {

        Teacher foundTeacher = teacherService.findById(999L);

        assertNull(foundTeacher);
    }
}
