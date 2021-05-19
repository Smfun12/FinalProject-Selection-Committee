package com.example.FinalProject.services;

import com.example.FinalProject.entities.models.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceTest {

    @MockBean
    StudentService studentService;

    @Test
    public void getFaculties() {

        Student student = Student.builder().login("login").build();
        List<Student> list = new ArrayList<>();
        list.add(student);
        list.add(Student.builder().login("user").build());
        when(studentService.getStudents()).thenReturn(list);

        List<Student> students = studentService.getStudents();
        assertEquals(list,students);

    }
    @Test
    public void findByTitle() {
        Optional<Student> student = Optional.of(Student.builder().login("username").build());
        when(studentService.findByLogin(student.get().getLogin())).thenReturn(student);
        Optional<Student> students = studentService.findByLogin("username");
        assertEquals(student,students);
    }


    @Test
    public void findStudentById() {
        Optional<Student> student = Optional.of(Student.builder().login("username").build());
        student.get().setStudentid(1);

        when(studentService.findStudentById(student.get().getStudentid())).thenReturn(student);
        Optional<Student> students = studentService.findStudentById(1);
        assertEquals(student,students);
    }

    @Test
    public void deleteStudentById() {
        Student student = Student.builder().login("username").build();
        studentService.deleteStudentById(student.getStudentid());
        verify(studentService, times(1)).deleteStudentById(student.getStudentid());
    }

}
