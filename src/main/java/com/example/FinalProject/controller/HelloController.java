package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/")
    public String hello() { // <--- 1
        return "hello"; // <--- 2
    }

    @GetMapping("/hello")
    public String greetingSubmit(
            @RequestParam(name = "name", required = false, defaultValue = "unknown")
            String student, Model model) {
        model.addAttribute("name", student);
        System.out.println(student);
        return "student";
    }

    @PostMapping("/")
    public String add(@RequestParam String login,@RequestParam String password, Model model) {
        Student student = new Student(login,password);
        studentRepository.save(student);
        Iterable<Student> iterable = studentRepository.findAll();
        model.addAttribute("students", iterable);
        return "student";
    }
    @PostMapping("/filter")
    public String find(@RequestParam String filter, Model model) {
        List<Student> students;
        if (filter != null && filter.isEmpty()) {
            students = studentRepository.findAll();

        }
        else{
            students = studentRepository.findByEmail(filter);
        }
        model.addAttribute("students", students);
        return "student";
    }
    @GetMapping("/students")
    public String main(Model model) {
        Iterable<Student> iterable = studentRepository.findAll();
        model.addAttribute("name", iterable);
        return "student";
    }

}