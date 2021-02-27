package com.example.FinalProject.services;

import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.StudentRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceTest {

    @MockBean
    StudentRepository studentRepository;

    @Test
    public void getStudents() {
        Student student = new Student("email@elai.com");
            studentRepository.save(student);
            assertEquals("email@elai.com", student.getEmail());
        //        when(studentRepository.findAll()).thenReturn(studentList);
//        verify(studentRepository, times(1)).findAll();
    }
}
