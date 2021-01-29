package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Roles;
import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private StudentRepository studentRepository;
    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addStudent(Student student, Model model){
        Student student1 = studentRepository.findByLogin(student.getLogin());
        if (student1 != null){
            model.addAttribute("student", "student exists");
            return "registration";
        }
        student1.setRolesSet(Collections.singleton(Roles.USER));
        studentRepository.save(student1);
        return "redirect:/login";
    }
}
