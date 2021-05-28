package com.example.FinalProject.pestistence.repository.facultyRepo;

import com.example.FinalProject.pestistence.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

        Optional<Faculty> findByTitle(String title);

        Optional<Faculty> findFacultyByFacultyid(long id);
}

