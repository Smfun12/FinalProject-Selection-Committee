package com.example.FinalProject.pestistence.repository.studentRepo;

import com.example.FinalProject.api.dto.HeaderStorage;
import com.example.FinalProject.domain.model.StudentModel;
import com.example.FinalProject.pestistence.entity.Student;
import com.example.FinalProject.pestistence.mapper.StudentMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepo implements StudentRepos{

    private final StudentRepository studentRepository;
    private final StudentJDBCRepo studentJDBCRepo;
    private final HeaderStorage headerStorage;

    public StudentRepo(StudentRepository studentRepository, StudentJDBCRepo studentJDBCRepo,
                       ApplicationContext applicationContext) {
        this.studentRepository = studentRepository;
        this.studentJDBCRepo = studentJDBCRepo;
        this.headerStorage = applicationContext.getBean("headerStorage", HeaderStorage.class);;
    }

    public List<StudentModel> getStudents() {
        if (headerStorage.getHeader().equals("jdbc")){
            return StudentMapper.INSTANCE.studentListToStudentModelList(studentJDBCRepo.getStudents());
        }
        else
            return StudentMapper.INSTANCE.studentListToStudentModelList(studentRepository.findAll());
    }

    public Optional<StudentModel> findByLogin(String login) {
        if (headerStorage.getHeader().equals("jdbc")){
            return StudentMapper.INSTANCE.studentToModelStudent(studentJDBCRepo.findByLogin(login).get());
        }
        return StudentMapper.INSTANCE.studentToModelStudent(studentRepository.findByLogin(login).get());
    }

    public Optional<StudentModel> findByEmail(String email) {
        if (headerStorage.getHeader().equals("jdbc")){
            return StudentMapper.INSTANCE.studentToModelStudent(studentJDBCRepo.findByEmail(email).get());
        }
        return StudentMapper.INSTANCE.studentToModelStudent(studentRepository.findByEmail(email).get());
    }

    public Optional<StudentModel> findStudentById(long id) {
        if (headerStorage.getHeader().equals("jdbc")){
            return StudentMapper.INSTANCE.studentToModelStudent(studentJDBCRepo.findStudentById(id).get());
        }
        return StudentMapper.INSTANCE.studentToModelStudent(studentRepository.findById(id).get());
    }

    public void saveStudent(StudentModel student) {
        if (headerStorage.getHeader().equals("jdbc")) {
            studentJDBCRepo.saveStudent(StudentMapper.INSTANCE.studentModelToStudent(student).get());
        }
        else
            studentRepository.save(StudentMapper.INSTANCE.studentModelToStudent(student).get());
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
            Optional<Student> student = studentJDBCRepo.findStudentById(id);
            student.ifPresent(value -> value.setEnabled(false));
            student.ifPresent(studentJDBCRepo::saveStudent);
        }
        else {
            Optional<Student> student = studentRepository.findStudentByStudentid(id);
            student.ifPresent(value -> value.setEnabled(false));
            student.ifPresent(studentRepository::save);
        }
    }

    public Page<StudentModel> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
//        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():
//                Sort.by(sortField).descending();
//        Pageable pageable = PageRequest.of(pageNo-1, pageSize,sort);
//        return studentRepository.findAll(pageable);
        return null;
    }

    public void enableStudentById(long studentid) {
        if (headerStorage.getHeader().equals("jdbc")){
            Optional<Student> student = studentJDBCRepo.findStudentById(studentid);
            student.ifPresent(value -> value.setEnabled(true));
            student.ifPresent(studentJDBCRepo::saveStudent);
        }
        else {
            Optional<Student> student = studentRepository.findStudentByStudentid(studentid);
            student.ifPresent(value -> value.setEnabled(true));
            student.ifPresent(studentRepository::save);
        }
    }

}
