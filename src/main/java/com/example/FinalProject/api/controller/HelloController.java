package com.example.FinalProject.api.controller;

import com.example.FinalProject.domain.model.FacultyModel;
import com.example.FinalProject.domain.model.StudentModel;
import com.example.FinalProject.domain.service.FacultyService;
import com.example.FinalProject.domain.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/api")
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
    public List<FacultyModel> finalizeResult(){
        List<FacultyModel> facultyList = facultyService.getFaculties();
        for (FacultyModel faculty : facultyList){
            int budgetPlaces = faculty.getBudgetPlaces();
            Set<StudentModel> students = faculty.getStudents();
            if (students == null){
                continue;
            }
            List<StudentModel> studentList = new ArrayList<>(students);
            Collections.sort(studentList);
            for (StudentModel studentModel : studentList){
                studentModel.setBudget(budgetPlaces-- > 0);
                studentService.saveStudent(studentModel);
            }
        }
        log.info("Finalizing result");
        return new LinkedList<>(facultyList);
    }
}