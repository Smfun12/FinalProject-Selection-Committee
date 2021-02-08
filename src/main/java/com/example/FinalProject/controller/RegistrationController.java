package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Roles;
import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.StudentRepository;
import com.example.FinalProject.services.StudentService;
import com.example.FinalProject.services.StudentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Controller
@Slf4j
public class RegistrationController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addStudent(@Valid Student student, BindingResult result, Model model){
        Optional<Student> student1 = studentService.findByLogin(student.getLogin());
        if (result.hasErrors()){
            model.addAttribute("error","Check user credentials");
            log.info("some error");
            return "registration";
        }
        if (student1.isPresent()) {
            model.addAttribute("student", "Student exists");
            log.info("Student" + student1 + " exists");
            return "registration";
        }
        Student student2 = Student.builder().
                email(student.getEmail())
                .login(student.getLogin())
                .password(student.getPassword())
                .city(student.getCity())
                .district(student.getDistrict())
                .school(student.getSchool())
                .rolesSet(student.getRolesSet())
                .build();
        log.info(student2.toString());
        studentService.saveStudent(student2);
        return "redirect:/login";
    }
}
