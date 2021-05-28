package com.example.FinalProject.pestistence.repository.facultyRepo;

import com.example.FinalProject.pestistence.entity.Faculty;
import com.example.FinalProject.pestistence.mapper.FacultyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FacultyJDBCRepo{

    JdbcTemplate jdbcTemplate;

    public List<Faculty> getFaculties() {
        return jdbcTemplate.query("SELECT * FROM faculties", new FacultyRowMapper());
    }

    public Optional<Faculty> findByTitle(String title) {
        return Optional.ofNullable(jdbcTemplate.queryForObject
                ("SELECT * FROM faculties where title=" + title, new FacultyRowMapper()));
    }

    public Optional<Faculty> findById(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject
                ("SELECT * FROM faculties where facultyid=" + id, new FacultyRowMapper()));
    }

    public void saveFaculty(Faculty faculty) {
        jdbcTemplate.update("INSERT INTO faculties VALUES(?,?,?,?,?,?,?,?)",
                faculty.getFacultyid(), faculty.getBudgetPlaces(),
                faculty.getContractPlaces(),faculty.getFirstSubject(),faculty.getSecondSubject(),
                faculty.getThirdSubject(),faculty.getTitle(),faculty.getTotalPlaces());
    }

    public void deleteById(long id) {
        jdbcTemplate.update("DELETE from faculties where facultyid=?", id);
    }
}
