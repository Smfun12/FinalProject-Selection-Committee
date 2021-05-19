package com.example.FinalProject.repository;

import com.example.FinalProject.entities.models.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
        Optional<Faculty> findByTitle(String title);

        Optional<Faculty> findFacultyByFacultyid(long id);
}

