package com.example.FinalProject.api.controller;

import com.example.FinalProject.domain.model.RolesModel;
import com.example.FinalProject.domain.model.StudentModel;
import com.example.FinalProject.domain.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@Slf4j
@RequestMapping("/api")
public class RegistrationController {

    private StudentService studentService;

    private PasswordEncoder passwordEncoder;

    ResourceBundleMessageSource messageSource;

    public RegistrationController(StudentService studentService, PasswordEncoder passwordEncoder) {
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
    }

    public RegistrationController(){
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("lang/res");
    }

    @GetMapping("/registration")
    public String registration() {
        log.info("Show registration page");
        return "registration";
    }

    @PostMapping("/registration")
    public StudentModel addStudent(@Valid StudentModel student, BindingResult result) {
        if (result.hasErrors()) {
            log.info(result.getAllErrors().toString());
            return null;
        }
        StudentModel student2 = new StudentModel(student.getStudentid(),
                student.getEmail(),
                student.getLogin(),
                passwordEncoder.encode(student.getPassword()),
                student.getCity(),
                student.getDistrict(),
                student.getSchool(),
                Collections.singleton(RolesModel.USER),
                false);
        log.info(student2.toString());
        try {
            studentService.saveStudent(student2);
        }
        catch (Exception e){
            log.info("Student" + student + " exists");
            return null;
        }
        log.info("Registered student successfully");
        return student;
    }
}
