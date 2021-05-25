package com.example.FinalProject.domain.service;

import com.example.FinalProject.domain.model.StudentModel;
import com.example.FinalProject.pestistence.entity.Student;
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

        StudentModel student = StudentModel.builder().login("login").build();
        List<StudentModel> list = new ArrayList<>();
        list.add(student);
        list.add(StudentModel.builder().login("user").build());
        when(studentService.getStudents()).thenReturn(list);

        List<StudentModel> students = studentService.getStudents();
        assertEquals(list,students);

    }
    @Test
    public void findByTitle() {
        Optional<StudentModel> student = Optional.of(StudentModel.builder().login("username").build());
        when(studentService.findByLogin(student.get().getLogin())).thenReturn(student);
        Optional<StudentModel> students = studentService.findByLogin("username");
        assertEquals(student,students);
    }


    @Test
    public void findStudentById() {
        Optional<StudentModel> student = Optional.of(StudentModel.builder().login("username").build());
        student.get().setStudentid(1);

        when(studentService.findStudentById(student.get().getStudentid())).thenReturn(student);
        Optional<StudentModel> students = studentService.findStudentById(1);
        assertEquals(student,students);
    }

    @Test
    public void deleteStudentById() {
        Student student = Student.builder().login("username").build();
        studentService.deleteStudentById(student.getStudentid());
        verify(studentService, times(1)).deleteStudentById(student.getStudentid());
    }

}
