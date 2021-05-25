package com.example.FinalProject.domain.service;

import com.example.FinalProject.domain.model.StudentModel;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<StudentModel> getStudents();
    Optional<StudentModel> findByLogin(String login);
    Optional<StudentModel> findByEmail(String email);
    Optional<StudentModel> findStudentById(long id);
    void saveStudent(StudentModel student);
    void deleteStudentById(long id);
    void disableStudentById(long id);
    Page<StudentModel> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    void enableStudentById(long studentid);
}