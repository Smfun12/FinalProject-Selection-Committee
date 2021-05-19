package com.example.FinalProject.repository;

import com.example.FinalProject.entities.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
        Optional<Student> findByEmail(String email);

        Optional<Student> findByLogin(String login);

        Optional<Student> findStudentByStudentid(long id);
}
