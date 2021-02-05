package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasAuthority('ADMIN')")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public String studentList(Model model){
        model.addAttribute("students",studentRepository.findAll());
        return "student";
    }

    @GetMapping("{student}")
    public String studentEdit(@PathVariable Student student, Model model){
        model.addAttribute("student",student);
        return "studentEdit";
    }

    @PostMapping
    public String studentSave(
            @RequestParam String login,
            @RequestParam Map<String, String> form,
            Student student){
        student.setLogin(login);
        studentRepository.save(student);
        return "redirect:/student";

    }}
