package com.example.FinalProject.domain.service;

import com.example.FinalProject.domain.model.StudentModel;

import java.util.List;
import java.util.NoSuchElementException;

public interface StudentService {
    List<StudentModel> getStudents();
    StudentModel findByLogin(String login) throws NoSuchElementException;
    StudentModel findByEmail(String email) throws NoSuchElementException;
    StudentModel findStudentById(long id);
    void saveStudent(StudentModel student);
    void deleteStudentById(long id);
    void disableStudentById(long id);

    void enableStudentById(long studentid);
}