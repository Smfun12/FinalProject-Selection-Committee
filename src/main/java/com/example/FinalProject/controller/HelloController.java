package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Faculty;
import com.example.FinalProject.entities.Student;
import com.example.FinalProject.services.FacultyService;
import com.example.FinalProject.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@Slf4j
public class HelloController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private FacultyService facultyService;

    @GetMapping("/")
    public String hello() { // <--- 1
        log.info("Show main page");
        return "mainPage"; // <--- 2
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/login")
    public String loginPage() {
        log.info("Show login page");
        return "mainPage";
    }

    @GetMapping("/finalize")
    public String finalizeResult(){
        List<Faculty> facultyList = facultyService.getFaculties();
        for (Faculty faculty : facultyList){
            int budgetPlaces = faculty.getBudgetPlaces();
            Set<Student> students = faculty.getStudents();
            List<Student> studentList = new ArrayList<>(students);
            Collections.sort(studentList);
            for (Student student : studentList){
                student.setBudget(budgetPlaces-- > 0);
                studentService.saveStudent(student);
            }
        }
        log.info("Finalizing result");
        return "redirect:/findFaculty";
    }
}