package com.example.FinalProject.api.mapper;

import com.example.FinalProject.domain.model.StudentModel;
import com.example.FinalProject.pestistence.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(source = "email", target = "email")
    Optional<StudentModel> studentToModelStudent(Student student);
    @Mapping(source = "email", target = "email")
    Optional<Student> studentModelTolStudent(StudentModel student);
}
