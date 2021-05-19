package com.example.FinalProject.repository;

import com.example.FinalProject.entities.dto.StudentDto;
import com.example.FinalProject.entities.models.Student;
import com.example.FinalProject.mappers.StudentMapper;
import com.example.FinalProject.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentJDBCRepo implements StudentService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentJDBCRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Student> getStudents() {
        return jdbcTemplate.query("SELECT * from students", new BeanPropertyRowMapper<>(Student.class));

    }

    @Override
    public Optional<Student> findByLogin(String login) {
        StudentDto studentDto = jdbcTemplate.queryForObject("SELECT * FROM students where login=" + login,
                new BeanPropertyRowMapper<>(StudentDto.class));
        return Optional.ofNullable(StudentMapper.INSTANCE.studentDtoToStudent(studentDto));
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT * from students where email=" + email,
                        new BeanPropertyRowMapper<>(Student.class)));
    }

    @Override
    public Optional<Student> findStudentById(long id) {
        StudentDto studentDto = findById(id);
        return Optional.ofNullable(StudentMapper.INSTANCE.studentDtoToStudent(studentDto));
    }

    @Override
    public void saveStudent(Student student) {
        StudentDto studentDto = StudentMapper.INSTANCE.studentToStudentDto(student);
        saveStudentToDb(studentDto);
    }

    @Override
    public void deleteStudentById(long id) {
        jdbcTemplate.update("DELETE from students where studentid=?", id);
    }

    @Override
    public void disableStudentById(long id) {
        StudentDto studentDto = findById(id);
        studentDto.setEnabled(true);
        saveStudentToDb(studentDto);
    }

    @Override
    public Page<Student> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        return null;
    }

    @Override
    public void enableStudentById(long studentid) {
        StudentDto studentDto = findById(studentid);
        studentDto.setEnabled(true);
        saveStudentToDb(studentDto);
    }

    private void saveStudentToDb(StudentDto studentDto) {
        jdbcTemplate.update("INSERT INTO students values (?,?,?,?,?,?,?,?,?,?,?,?,?)",
                studentDto.getStudentid(), studentDto.getEmail(), studentDto.getLogin(), studentDto.getPassword(),
                studentDto.getCity(), studentDto.getDistrict(), studentDto.getSchool(), studentDto.getFirstGrade(),
                studentDto.getSecondGrade(), studentDto.getThirdGrade());
    }

    private StudentDto findById(long id) {
        return (jdbcTemplate.queryForObject("select * from students where studentid=" + id,
                new BeanPropertyRowMapper<>(StudentDto.class)));
    }
}
