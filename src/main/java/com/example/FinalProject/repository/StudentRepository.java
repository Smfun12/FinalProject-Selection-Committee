package com.example.FinalProject.repository;

import com.example.FinalProject.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
        List<Student> findByEmail(String email);

        Optional<Student> findByLogin(String login);

        Optional<Student> findStudentByStudentid(long id);

        void deleteStudentByStudentid(long id);
}
