package com.example.FinalProject.services;

import com.example.FinalProject.entities.models.Faculty;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface FacultyService {
    List<Faculty> getFaculties();
    Optional<Faculty> findByTitle(String title);
    Optional<Faculty> findByFacultyById(long id);
    void saveFaculty(Faculty faculty);
    void deleteFacultyById(long id);
    Page<Faculty> findFacultyPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}