package com.example.FinalProject.domain.service;

import com.example.FinalProject.domain.model.FacultyModel;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface FacultyService {
    List<FacultyModel> getFaculties();
    Optional<FacultyModel> findByTitle(String title);
    Optional<FacultyModel> findByFacultyById(long id);
    void saveFaculty(FacultyModel faculty);
    void deleteFacultyById(long id);
    Page<FacultyModel> findFacultyPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}