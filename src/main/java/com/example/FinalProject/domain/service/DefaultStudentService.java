package com.example.FinalProject.domain.service;

import com.example.FinalProject.domain.model.StudentModel;
import com.example.FinalProject.pestistence.repository.studentRepo.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DefaultStudentService implements StudentService{

    StudentRepo studentRepo;

    public DefaultStudentService(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    @Override
    public List<StudentModel> getStudents() {
        return studentRepo.getStudents();
    }

    @Override
    public StudentModel findByLogin(String login) {
        return studentRepo.findByLogin(login);
    }

    @Override
    public StudentModel findByEmail(String email) throws NoSuchElementException {
        return studentRepo.findByEmail(email);
    }

    @Override
    public StudentModel findStudentById(long id) {
        return studentRepo.findStudentById(id);
    }

    @Override
    public void saveStudent(StudentModel student) {
        studentRepo.saveStudent(student);
    }

    @Override
    public void deleteStudentById(long id) {
        studentRepo.deleteStudentById(id);
    }

    @Override
    public void disableStudentById(long id) {
        studentRepo.disableStudentById(id);
    }

    @Override
    public void enableStudentById(long studentid) {
        studentRepo.enableStudentById(studentid);
    }
}
