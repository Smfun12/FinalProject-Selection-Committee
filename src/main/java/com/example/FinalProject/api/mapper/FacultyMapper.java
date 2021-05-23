package com.example.FinalProject.api.mapper;

import com.example.FinalProject.domain.model.FacultyModel;
import com.example.FinalProject.domain.model.StudentModel;
import com.example.FinalProject.pestistence.entity.Faculty;
import com.example.FinalProject.pestistence.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface FacultyMapper {
    FacultyMapper INSTANCE = Mappers.getMapper(FacultyMapper.class);

    @Mapping(source = "email", target = "email")
    Optional<FacultyModel> facultyToModelFaculty(Faculty faculty);
    @Mapping(source = "email", target = "email")
    Optional<Faculty> facultyModelToFaculty(FacultyModel student);
}
