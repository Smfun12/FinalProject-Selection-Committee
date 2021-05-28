package com.example.FinalProject.pestistence.repository.studentRepo;

import com.example.FinalProject.domain.model.StudentModel;

import java.util.List;
public interface DefaultStudentRepository {

    List<StudentModel> getStudents();

    StudentModel findByLogin(String login);

    StudentModel findByEmail(String email);

    StudentModel findStudentById(long id);

    void saveStudent(StudentModel student);

    void deleteStudentById(long id);

    void disableStudentById(long id);

    void enableStudentById(long studentid);

}
