package com.example.FinalProject.pestistence.repository.studentRepo;

import com.example.FinalProject.api.dto.HeaderStorage;
import com.example.FinalProject.domain.model.StudentModel;
import com.example.FinalProject.pestistence.entity.Student;
import com.example.FinalProject.pestistence.mapper.StudentMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class StudentRepo implements DefaultStudentRepository{

    private final StudentRepository studentRepository;
    private final StudentJDBCRepo studentJDBCRepo;
    private final HeaderStorage headerStorage;

    public StudentRepo(StudentRepository studentRepository, StudentJDBCRepo studentJDBCRepo) {
        this.studentRepository = studentRepository;
        this.studentJDBCRepo = studentJDBCRepo;
        this.headerStorage = new HeaderStorage();
    }

    public List<StudentModel> getStudents() {
        if (headerStorage.getHeader().equals("jdbc")){
            return StudentMapper.INSTANCE.studentListToStudentModelList(studentJDBCRepo.getStudents());
        }
        else
            return StudentMapper.INSTANCE.studentListToStudentModelList(studentRepository.findAll());
    }

    public StudentModel findByLogin(String login) {
        Optional<Student> byLogin;
        if (headerStorage.getHeader().equals("jdbc")){
            byLogin = studentJDBCRepo.findByLogin(login);
        }
        else {
            byLogin = studentRepository.findByLogin(login);

        }
        if (!byLogin.isPresent()){
            throw new NoSuchElementException("Student with such login not found");
        }
        return StudentMapper.INSTANCE.studentToModelStudent(byLogin.get());
    }

    public StudentModel findByEmail(String email) throws NoSuchElementException{
        Optional<Student> byEmail;
        if (headerStorage.getHeader().equals("jdbc")){
            byEmail = studentJDBCRepo.findByEmail(email);
        }
        else {
            byEmail = studentRepository.findByEmail(email);
        }
        if (!byEmail.isPresent())
            throw new NoSuchElementException("No student with such email found");
        return StudentMapper.INSTANCE.studentToModelStudent(byEmail.get());
    }

    public StudentModel findStudentById(long id) {
        Optional<Student> byId;
        if (headerStorage.getHeader().equals("jdbc")){
            byId = studentJDBCRepo.findStudentById(id);
        }
        else {
            byId = studentRepository.findStudentByStudentid(id);
        }
        if (!byId.isPresent())
            throw new NoSuchElementException("No student with such id found");
        return StudentMapper.INSTANCE.studentToModelStudent(byId.get());
    }

    public void saveStudent(StudentModel student) {
        if (headerStorage.getHeader().equals("jdbc")) {
            studentJDBCRepo.saveStudent(StudentMapper.INSTANCE.studentModelToStudent(student));
        }
        else
            studentRepository.save(StudentMapper.INSTANCE.studentModelToStudent(student));
    }

    public void deleteStudentById(long id) {
        if (headerStorage.getHeader().equals("jdbc")) {
            studentJDBCRepo.deleteStudentById(id);
        }
        else
            studentRepository.deleteById(id);
    }

    public void disableStudentById(long id) {
        if (headerStorage.getHeader().equals("jdbc")){
            studentJDBCRepo.disableStudentById(id);
        }
        else {
            Optional<Student> student = studentRepository.findStudentByStudentid(id);
            student.ifPresent(value -> value.setEnabled(false));
            student.ifPresent(studentRepository::save);
        }
    }

    public void enableStudentById(long studentid) {
        if (headerStorage.getHeader().equals("jdbc")){
            studentJDBCRepo.enableStudentById(studentid);
        }
        else {
            Optional<Student> student = studentRepository.findStudentByStudentid(studentid);
            student.ifPresent(value -> value.setEnabled(true));
            student.ifPresent(studentRepository::save);
        }
    }

}
