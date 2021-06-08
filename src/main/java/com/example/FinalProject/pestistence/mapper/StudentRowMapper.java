package com.example.FinalProject.pestistence.mapper;

import com.example.FinalProject.pestistence.entity.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {
        Student student = new Student();
        student.setStudentid(resultSet.getLong("studentid"));
        student.setLogin(resultSet.getString("login"));
        student.setBudget(resultSet.getBoolean("budget"));
        student.setCity(resultSet.getString("city"));
        student.setDistrict(resultSet.getString("district"));
        student.setEmail(resultSet.getString("email"));
        student.setEnabled(resultSet.getBoolean("enabled"));
        student.setPassword(resultSet.getString("password"));
        student.setSchool(resultSet.getString("school"));
        student.setFirstGrade(resultSet.getInt("first_grade"));
        student.setSecondGrade(resultSet.getInt("second_grade"));
        student.setThirdGrade(resultSet.getInt("third_grade"));
        return student;
    }
}
