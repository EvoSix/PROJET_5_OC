package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
public class TeacherTest {

    @Test
    void testTeacherEntity(){
        Teacher teacher = new Teacher();
        teacher.equals(new Teacher());
        teacher.hashCode();
        teacher.toString();
        assertNotNull(teacher.toString());
    }

    @Test
    void testTeacherEntityBuilder(){
        Teacher teacher = new Teacher();
        teacher.equals(Teacher.builder().build());
        assertNotNull(teacher.toString());
    }
}