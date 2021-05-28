package com.example.FinalProject.pestistence.repository.facultyRepo;

import com.example.FinalProject.domain.model.FacultyModel;

import java.util.List;

public interface DefaultFacultyRepository {

    List<FacultyModel> getFaculties();

    FacultyModel findByTitle(String login);

    FacultyModel findById(long id);

    void saveFaculty(FacultyModel student);

    void deleteById(long id);
}
