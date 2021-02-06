package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Faculty;
import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.FacultyRepository;
import com.example.FinalProject.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HelloController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;

    @GetMapping("/")
    public String hello() { // <--- 1
        return "mainPage"; // <--- 2
    }
    @PostMapping("/")
    public String helloPage() { // <--- 1
        return "mainPage"; // <--- 2
    }

    @GetMapping("/findStudent")
    public String findStudent(Model model) {
        Iterable<Student> iterable = studentRepository.findAll();
        model.addAttribute("students", iterable);
        return "findStudent";
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/findFaculty")
    public String findFaculty(Model model) {
        Iterable<Faculty> iterable = facultyRepository.findAll();
        model.addAttribute("faculties", iterable);
        return "findFaculty";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/filterStudent")
    public String find(@RequestParam String filter, Model model) {
        List<Student> students;
        if (filter != null && filter.isEmpty()) {
            students = studentRepository.findAll();

        } else {
            students = studentRepository.findByEmail(filter);
        }
        model.addAttribute("students", students);
        return "findStudent";
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/filterFaculty")
    public String findFaculty(@RequestParam String filter, Model model) {
        List<Faculty> faculties;
        if (filter != null && filter.isEmpty()) {
            faculties = facultyRepository.findAll();

        } else {
            faculties = facultyRepository.findByTitle(filter);
        }
        model.addAttribute("faculties", faculties);
        return "findFaculty";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/students")
    public String main(Model model) {
        Iterable<Student> iterable = studentRepository.findAll();
        model.addAttribute("name", iterable);
        return "findStudent";
    }

    @GetMapping("/addFaculty")
    public String addFacultyPage(){
        return "addFaculty";
    }
}