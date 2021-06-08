package com.example.FinalProject.pestistence.mapper;

import com.example.FinalProject.domain.model.FacultyModel;
import com.example.FinalProject.pestistence.entity.Faculty;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FacultyMapper {
    FacultyMapper INSTANCE = Mappers.getMapper(FacultyMapper.class);

    FacultyModel facultyToModelFaculty(Faculty faculty);
    Faculty facultyModelToFaculty(FacultyModel student);
    List<FacultyModel> facultyListToFacultyModelList(List<Faculty> faculties);
}
