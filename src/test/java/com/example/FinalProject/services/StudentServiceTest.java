package com.example.FinalProject.services;

import com.example.FinalProject.entities.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceTest {

    @MockBean
    StudentServiceImpl studentService;

    @Test
    public void getStudents() {

        Student student = Student.builder().email("email@mail.com").build();
        List<Student> list = new ArrayList<>();
        list.add(student);
        list.add(new Student("emailma.com"));
        when(studentService.getStudents()).thenReturn(list);
    }
    @Test
    public void findByLogin() {
        Optional<Student> student = Optional.of(Student.builder().email("email@mail.com").build());

        when(studentService.findByLogin(student.get().getLogin())).thenReturn(student);
    }

    @Test
    public void findByEmail() {
        List<Student> student = new ArrayList<>();
        student.add(Student.builder().email("email@mail.com").build());
        student.add(Student.builder().email("user@mail.com").build());

        when(studentService.findByEmail("email@email.com")).thenReturn(student);
    }

    @Test
    public void findStudentById() {
        Optional<Student> student = Optional.of(Student.builder().email("email@mail.com").build());
        student.get().setStudentid(1);

        when(studentService.findStudentById(student.get().getStudentid())).thenReturn(student);
    }

    @Test
    public void saveStudent() {
        Student student = Student.builder().email("email@mail.com").build();
        studentService.saveStudent(student);
        verify(studentService, times(1)).saveStudent(student);
    }

    @Test
    public void deleteStudentById() {
        Student student = Student.builder().email("email@mail.com").build();
        studentService.deleteStudentById(student.getStudentid());
        verify(studentService, times(1)).deleteStudentById(student.getStudentid());
    }

}
