package com.example.FinalProject.pestistence.mapper;

import com.example.FinalProject.domain.model.FacultyModel;
import com.example.FinalProject.pestistence.entity.Faculty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FacultyMapper {
    FacultyMapper INSTANCE = Mappers.getMapper(FacultyMapper.class);

    @Mapping(source = "title", target = "title")
    Optional<FacultyModel> facultyToModelFaculty(Faculty faculty);
    @Mapping(source = "title", target = "title")
    Optional<Faculty> facultyModelToFaculty(FacultyModel student);
    @Mapping(source = "facultyid", target = "facultyid")
    List<FacultyModel> facultyListToFacultyModelList(List<Faculty> faculties);
    @Mapping(source = "facultyid", target = "facultyid")
    List<Faculty> facultyModelListToFacultyList(List<FacultyModel> facultyModels);
}
