package com.example.FinalProject.api.controller;

import com.example.FinalProject.domain.service.FacultyService;
import com.example.FinalProject.domain.service.StudentService;
import com.example.FinalProject.pestistence.entity.Faculty;
import com.example.FinalProject.pestistence.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Slf4j
public class HelloController {

    private final StudentService studentService;
    private final FacultyService facultyService;

    public HelloController(StudentService studentService, FacultyService facultyService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
    }

    @GetMapping("/")
    public String hello() { // <--- 1
        log.info("Show main page");
        return "mainPage"; // <--- 2
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/login")
    public String loginPage() {
        log.info("Show login page");
        return "loginPage";
    }

    @GetMapping("/finalize")
    public List<Faculty> finalizeResult(){
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
        return new LinkedList<>(facultyList);
    }
}