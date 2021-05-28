package com.example.FinalProject.domain.service;

import com.example.FinalProject.domain.model.FacultyModel;

import java.util.List;

public interface FacultyService {
    List<FacultyModel> getFaculties();
    FacultyModel findByTitle(String title);
    FacultyModel findByFacultyById(long id);
    void saveFaculty(FacultyModel faculty);
    void deleteFacultyById(long id);
}