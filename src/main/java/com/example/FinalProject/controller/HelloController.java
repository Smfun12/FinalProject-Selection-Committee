package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Roles;
import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
public class HelloController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/")
    public String hello() { // <--- 1
        return "hello"; // <--- 2
    }
    @PostMapping("/")
    public String helloPage() { // <--- 1
        return "hello"; // <--- 2
    }

    @GetMapping("/hello")
    public String greetingSubmit(
            @RequestParam(name = "name", required = false, defaultValue = "unknown")
                    String student, Model model) {
        model.addAttribute("name", student);
        return "findStudent";
    }

    @PostMapping("/findStudent")
    public String add(@RequestParam String login, @RequestParam String password,
                      Model model, @Valid Student student1, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "hello";
        Iterable<Student> iterable = studentRepository.findAll();
        model.addAttribute("students", iterable);
        return "findStudent";
    }

    @PostMapping("/filter")
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

}