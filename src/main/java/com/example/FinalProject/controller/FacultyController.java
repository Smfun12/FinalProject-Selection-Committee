package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Faculty;
import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.FacultyRepository;
import com.example.FinalProject.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/faculty")
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class FacultyController {

    @Autowired
    private FacultyRepository facultyRepository;



    @GetMapping
    public String facultyList(Model model){
        model.addAttribute("faculties",facultyRepository.findAll());
        return "findFaculty";
    }

    @GetMapping("{faculty}")
    public String facultyEdit(@PathVariable Faculty faculty, Model model){
        model.addAttribute("faculties",faculty);
        return "facultyEdit";
    }
    @PostMapping("/addFaculty")
    public String addFaculty(
            @Valid Faculty faculty, BindingResult result, Model model){
        List<Faculty> facultyList= facultyRepository.findByTitle(faculty.getTitle());
        if (result.hasErrors()){
            model.addAttribute("error","Check faculty credentials");
            log.info("some error");
            return "addFaculty";
        }
        for (Faculty faculty1 : facultyList) {
            if (faculty.getTitle().equals(faculty1.getTitle())) {
                model.addAttribute("faculty", "Faculty exists");
                log.info("Faculty" + facultyList + " exists");
                return "addFaculty";
            }
        }
        Faculty faculty2 = Faculty.builder()
                .title(faculty.getTitle())
                .totalPlaces(faculty.getTotalPlaces())
                .budgetPlaces(faculty.getBudgetPlaces())
                .contractPlaces(faculty.getContractPlaces())
                .build();
        facultyRepository.save(faculty2);
        return "redirect:/findFaculty";
    }}
