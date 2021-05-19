package com.example.FinalProject.mappers;

import com.example.FinalProject.entities.dto.StudentDto;
import com.example.FinalProject.entities.models.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(source = "email", target = "email")
    StudentDto studentToStudentDto(Student student);
    @Mapping(source = "email", target = "email")
    Student studentDtoToStudent(StudentDto student);
}
