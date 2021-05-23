package com.example.FinalProject.api.mapper;

import com.example.FinalProject.pestistence.entity.Faculty;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FacultyRowMapper implements RowMapper<Faculty> {
    @Override
    public Faculty mapRow(ResultSet resultSet, int i) throws SQLException {
        Faculty faculty = new Faculty();
        faculty.setFacultyid(resultSet.getLong("facultyid"));
        faculty.setBudgetPlaces(resultSet.getInt("budget_places"));
        faculty.setContractPlaces(resultSet.getInt("contract_places"));
        faculty.setTotalPlaces(resultSet.getInt("total_places"));
        faculty.setTitle(resultSet.getString("title"));
        faculty.setFirstSubject(resultSet.getString("first_subject"));
        faculty.setSecondSubject(resultSet.getString("second_subject"));
        faculty.setThirdSubject(resultSet.getString("third_subject"));
        return faculty;
    }
}
