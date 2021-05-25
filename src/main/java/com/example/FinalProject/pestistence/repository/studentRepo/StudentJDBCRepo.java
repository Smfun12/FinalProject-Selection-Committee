package com.example.FinalProject.pestistence.repository.studentRepo;

import com.example.FinalProject.pestistence.mapper.StudentRowMapper;
import com.example.FinalProject.pestistence.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentJDBCRepo{

    private final JdbcTemplate jdbcTemplate;

    public StudentJDBCRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> getStudents() {
        return jdbcTemplate.query("SELECT * from students", new StudentRowMapper());

    }

    public Optional<Student> findByLogin(String login) {
        Student student = jdbcTemplate.queryForObject("SELECT * FROM students where login=" + login,
                new StudentRowMapper());
        return Optional.ofNullable(student);
    }

    public Optional<Student> findByEmail(String email) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT * from students where email=" + email,
                        new StudentRowMapper()));
    }

    public Optional<Student> findStudentById(long id) {
        Student student = findById(id);
        return Optional.ofNullable(student);
    }

    public void saveStudent(Student student) {
        saveStudentToDb(student);
    }

    public void deleteStudentById(long id) {
        jdbcTemplate.update("DELETE from students where studentid=?", id);
    }

    public void disableStudentById(long id) {
        Student student = findById(id);
        student.setEnabled(true);
        saveStudentToDb(student);
    }

    public Page<Student> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        List<Student> query = jdbcTemplate.query("SELECT * from students", new StudentRowMapper());
        return new PageImpl<>(query);
    }

    public void enableStudentById(long studentid) {
        Student student = findById(studentid);
        student.setEnabled(true);
        saveStudentToDb(student);
    }

    private void saveStudentToDb(Student student) {
        jdbcTemplate.update("INSERT INTO students values (?,?,?,?,?,?,?,?,?,?,?,?,?)",
                student.getStudentid(), student.getEmail(), student.getLogin(), student.getPassword(),
                student.getCity(), student.getDistrict(), student.getSchool(), student.getFirstGrade(),
                student.getSecondGrade(), student.getThirdGrade());
    }

    private Student findById(long id) {
        return jdbcTemplate.queryForObject("select * from students where studentid=" + id,
                new StudentRowMapper());
    }
}
