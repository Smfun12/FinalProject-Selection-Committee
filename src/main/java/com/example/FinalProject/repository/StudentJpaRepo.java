package com.example.FinalProject.repository;

import com.example.FinalProject.entities.models.Student;
import com.example.FinalProject.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentJpaRepo implements StudentService {

    StudentRepository studentRepository;

    @Autowired
    public StudentJpaRepo(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findByLogin(String login) {
        return studentRepository.findByLogin(login);
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    @Override
    public Optional<Student> findStudentById(long id) {
        return studentRepository.findStudentByStudentid(id);
    }

    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void deleteStudentById(long id) {
        Optional<Student> student = studentRepository.findById(id);
        studentRepository.delete(student.get());
    }

    @Override
    public void disableStudentById(long id) {
        Optional<Student> student = studentRepository.findById(id);
        student.get().setEnabled(false);
        studentRepository.save(student.get());
    }

    @Override
    public Page<Student> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize,sort);
        return studentRepository.findAll(pageable);
    }

    @Override
    public void enableStudentById(long studentid) {
        Optional<Student> student = studentRepository.findById(studentid);
        student.get().setEnabled(true);
        studentRepository.save(student.get());
    }
}
