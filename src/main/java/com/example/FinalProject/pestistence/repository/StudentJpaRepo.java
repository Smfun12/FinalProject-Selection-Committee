package com.example.FinalProject.pestistence.repository;

import com.example.FinalProject.pestistence.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentJpaRepo {

    StudentRepository studentRepository;

    public StudentJpaRepo(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> findByLogin(String login) {
        return studentRepository.findByLogin(login);
    }

    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public Optional<Student> findStudentById(long id) {
        return studentRepository.findStudentByStudentid(id);
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public void deleteStudentById(long id) {
        studentRepository.deleteById(id);
    }

    public void disableStudentById(long id) {
        Optional<Student> student = studentRepository.findStudentByStudentid(id);
        student.ifPresent(value -> value.setEnabled(false));
        student.ifPresent(value -> studentRepository.save(value));
    }

    public Page<Student> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize,sort);
        return studentRepository.findAll(pageable);
    }

    public void enableStudentById(long studentid) {
        Optional<Student> student = studentRepository.findStudentByStudentid(studentid);
        student.ifPresent(value -> value.setEnabled(true));
        student.ifPresent(value -> studentRepository.save(value));
    }

}
