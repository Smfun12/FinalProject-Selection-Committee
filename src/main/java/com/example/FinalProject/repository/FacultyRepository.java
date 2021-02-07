package com.example.FinalProject.repository;

import com.example.FinalProject.entities.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
        Optional<Faculty> findByTitle(String title);

        List<Faculty> findByTotalPlaces(int totalPlaces);

        List<Faculty> findByBudgetPlaces(int budgetPlaces);

}

