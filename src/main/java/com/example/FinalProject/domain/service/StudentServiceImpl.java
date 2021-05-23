package com.example.FinalProject.domain.service;

import com.example.FinalProject.api.dto.SaveHeaders;
import com.example.FinalProject.pestistence.entity.Student;
import com.example.FinalProject.pestistence.repository.StudentJDBCRepo;
import com.example.FinalProject.pestistence.repository.StudentJpaRepo;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service

public class StudentServiceImpl implements StudentService{

    StudentJDBCRepo studentJDBCRepo;
    StudentJpaRepo studentJpaRepo;
    ApplicationContext applicationContext;
    SaveHeaders saveHeaders;

    public StudentServiceImpl(StudentJDBCRepo studentJDBCRepo, StudentJpaRepo studentJpaRepo, ApplicationContext applicationContext) {
        this.studentJDBCRepo = studentJDBCRepo;
        this.studentJpaRepo = studentJpaRepo;
        this.applicationContext = applicationContext;
        this.saveHeaders = applicationContext.getBean("saveHeaders", SaveHeaders.class);
    }

    @Override
    public List<Student> getStudents() {
        if (saveHeaders.getHeader() != null && saveHeaders.getHeader().equals("jdbc")){
            return studentJDBCRepo.getStudents();
        }
        else {
            return studentJpaRepo.getStudents();
        }
    }

    @Override
    public Optional<Student> findByLogin(String login) {
        if (saveHeaders.getHeader() != null && saveHeaders.getHeader().equals("jdbc")){
            return studentJDBCRepo.findByLogin(login);
        }
        else {
            return studentJpaRepo.findByLogin(login);
        }
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        if (saveHeaders.getHeader() != null && saveHeaders.getHeader().equals("jdbc")){
            return studentJDBCRepo.findByEmail(email);
        }
        else {
            return studentJpaRepo.findByEmail(email);
        }
    }

    @Override
    public Optional<Student> findStudentById(long id) {
        if (saveHeaders.getHeader() != null && saveHeaders.getHeader().equals("jdbc")){
            return studentJDBCRepo.findStudentById(id);
        }
        else {
            return studentJpaRepo.findStudentById(id);
        }
    }

    @Override
    public void saveStudent(Student student) {
        if (saveHeaders.getHeader() != null && saveHeaders.getHeader().equals("jdbc")){
            studentJDBCRepo.saveStudent(student);
        }
        else {
            studentJpaRepo.saveStudent(student);
        }
    }

    @Override
    public void deleteStudentById(long id) {
        if (saveHeaders.getHeader() != null && saveHeaders.getHeader().equals("jdbc")){
            studentJDBCRepo.deleteStudentById(id);
        }
        else {
            studentJpaRepo.deleteStudentById(id);
        }
    }

    @Override
    public void disableStudentById(long id) {
        if (saveHeaders.getHeader() != null && saveHeaders.getHeader().equals("jdbc")){
            studentJDBCRepo.disableStudentById(id);
        }
        else {
            studentJpaRepo.disableStudentById(id);
        }
    }

    @Override
    public Page<Student> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        if (saveHeaders.getHeader() != null && saveHeaders.getHeader().equals("jdbc")){
            return (Page<Student>) studentJDBCRepo.getStudents();
        }
        else {
            return studentJpaRepo.findPaginated(pageNo,pageSize,sortField,sortDirection);
        }
    }

    @Override
    public void enableStudentById(long studentid) {
        if (saveHeaders.getHeader() != null && saveHeaders.getHeader().equals("jdbc")){
            studentJDBCRepo.enableStudentById(studentid);
        }
        else {
            studentJpaRepo.enableStudentById(studentid);
        }
    }
}
