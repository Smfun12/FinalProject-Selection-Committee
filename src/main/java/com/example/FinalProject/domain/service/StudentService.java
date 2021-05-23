package com.example.FinalProject.domain.service;

import com.example.FinalProject.pestistence.entity.Student;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> getStudents();
    Optional<Student> findByLogin(String login);
    Optional<Student> findByEmail(String email);
    Optional<Student> findStudentById(long id);
    void saveStudent(Student student);
    void deleteStudentById(long id);
    void disableStudentById(long id);
    Page<Student> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    void enableStudentById(long studentid);
}