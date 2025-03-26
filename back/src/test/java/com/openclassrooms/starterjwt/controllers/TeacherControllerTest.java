package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(MockitoExtension.class)

public class TeacherControllerTest {


    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    @DisplayName("Get Teacher by ID, When exist")
    void findById_ShouldReturnTeacher_WhenExists() throws Exception {

        Teacher teacher = new Teacher(1L, "Doe", "John", null, null);
        TeacherDto teacherDto = new TeacherDto(1L, "Doe", "John", null, null);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("Get Teacher by ID, when not exist")
    void findById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(teacherService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get Teacher by ID, when invalid")
    void findById_ShouldReturnBadRequest_WhenInvalidId() throws Exception {
        mockMvc.perform(get("/api/teacher/abc")) // ID non valide
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get All Teachers, Success")
    void findAll_ShouldReturnListOfTeachers() throws Exception {

        Teacher teacher1 = new Teacher(1L, "Doe", "John", null, null);
        Teacher teacher2 = new Teacher(2L, "Smith", "Jane", null, null);
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        TeacherDto teacherDto1 = new TeacherDto(1L, "Doe", "John", null, null);
        TeacherDto teacherDto2 = new TeacherDto(2L, "Smith", "Jane", null, null);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(Arrays.asList(teacherDto1, teacherDto2));

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
}
