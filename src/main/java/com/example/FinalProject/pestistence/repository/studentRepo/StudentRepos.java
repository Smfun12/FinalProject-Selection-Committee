package com.example.FinalProject.pestistence.repository.studentRepo;

import com.example.FinalProject.domain.model.StudentModel;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface StudentRepos {

    List<StudentModel> getStudents();

    Optional<StudentModel> findByLogin(String login);

    Optional<StudentModel> findByEmail(String email);

    Optional<StudentModel> findStudentById(long id);

    void saveStudent(StudentModel student);

    Page<StudentModel> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    void deleteStudentById(long id);

    void disableStudentById(long id);

    void enableStudentById(long studentid);

}
