package com.example.FinalProject.repository;

import com.example.FinalProject.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
        List<Student> findByEmail(String login);
}
