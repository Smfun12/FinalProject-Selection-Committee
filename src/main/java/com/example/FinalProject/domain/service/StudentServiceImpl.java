package com.example.FinalProject.domain.service;

import com.example.FinalProject.domain.model.StudentModel;
import com.example.FinalProject.pestistence.repository.studentRepo.StudentRepos;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{

    StudentRepos studentRepos;

    public StudentServiceImpl(StudentRepos studentRepos) {
        this.studentRepos = studentRepos;
    }

    @Override
    public List<StudentModel> getStudents() {
        return studentRepos.getStudents();
    }

    @Override
    public Optional<StudentModel> findByLogin(String login) {
        return studentRepos.findByLogin(login);
    }

    @Override
    public Optional<StudentModel> findByEmail(String email) {
        return studentRepos.findByEmail(email);
    }

    @Override
    public Optional<StudentModel> findStudentById(long id) {
        return studentRepos.findStudentById(id);
    }

    @Override
    public void saveStudent(StudentModel student) {
        studentRepos.saveStudent(student);
    }

    @Override
    public void deleteStudentById(long id) {
        studentRepos.deleteStudentById(id);
    }

    @Override
    public void disableStudentById(long id) {
        studentRepos.disableStudentById(id);
    }

    @Override
    public Page<StudentModel> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        return studentRepos.findPaginated(pageNo,pageSize,sortField,sortDirection);
    }

    @Override
    public void enableStudentById(long studentid) {

    }
}
