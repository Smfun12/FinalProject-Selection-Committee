package com.example.FinalProject.pestistence.mapper;

import com.example.FinalProject.domain.model.StudentModel;
import com.example.FinalProject.pestistence.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    StudentModel studentToModelStudent(Student student);
    Student studentModelToStudent(StudentModel student);
    List<StudentModel> studentListToStudentModelList(List<Student> students);
}
