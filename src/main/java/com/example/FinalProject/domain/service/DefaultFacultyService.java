package com.example.FinalProject.domain.service;

import com.example.FinalProject.domain.model.FacultyModel;
import com.example.FinalProject.pestistence.repository.facultyRepo.FacultyRepo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Faculty service class
 * Transfer db operations to repository
 */
@Service
public class DefaultFacultyService implements FacultyService{

    FacultyRepo facultyRepo;

    public DefaultFacultyService(FacultyRepo facultyRepo) {
        this.facultyRepo = facultyRepo;
    }

    @Override
    public List<FacultyModel> getFaculties() {
        return facultyRepo.getFaculties();
    }

    @Override
    public FacultyModel findByTitle(String title) {
        return facultyRepo.findByTitle(title);
    }

    @Override
    public FacultyModel findByFacultyById(long id) {
        return facultyRepo.findById(id);
    }

    @Override
    public void saveFaculty(FacultyModel faculty) {
        facultyRepo.saveFaculty(faculty);
    }

    @Override
    public void deleteFacultyById(long id) {
        facultyRepo.deleteById(id);
    }
}
