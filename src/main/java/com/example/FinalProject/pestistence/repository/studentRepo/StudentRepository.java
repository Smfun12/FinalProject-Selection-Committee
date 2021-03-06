package com.example.FinalProject.pestistence.repository.studentRepo;

import com.example.FinalProject.pestistence.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    Optional<Student> findByLogin(String login);

    Optional<Student> findStudentByStudentid(long id);
}
