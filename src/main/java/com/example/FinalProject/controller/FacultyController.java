package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Faculty;
import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.FacultyRepository;
import com.example.FinalProject.repository.StudentRepository;
import com.example.FinalProject.services.FacultyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @GetMapping("/findFaculty")
    public String facultyList(Model model){
        return findFacultyPaginated(1,"title","asc",model);
    }

    @PostMapping("/faculty/saveFaculty")
    public String saveFaculty(
            @ModelAttribute("faculty") Faculty faculty){
        facultyService.saveFaculty(faculty);
        log.info("edit faculty successful");
        return "redirect:/findFaculty";

    }

    @GetMapping("/faculty/{faculty}")
    public String facultyEdit(@PathVariable Faculty faculty, Model model){
        model.addAttribute("faculty",faculty);
        return "facultyEdit";
    }

    @GetMapping("/faculty/addFaculty")
    public String addFacultyPage(){
        return "addFaculty";
    }

    @PostMapping("/faculty/addFaculty")
    public String addFaculty(
            @Valid Faculty faculty, BindingResult result, Model model){
        Optional<Faculty> facultyList= facultyService.findByTitle(faculty.getTitle());
        if (result.hasErrors()){
            model.addAttribute("error","Check faculty credentials");
            log.info("some error");
            return "addFaculty";
        }
        if (facultyList.isPresent()){
            model.addAttribute("faculty", "Faculty exists");
            log.info("Faculty" + facultyList + " exists");
            return "addFaculty";
        }
        Faculty faculty2 = Faculty.builder()
                .title(faculty.getTitle())
                .totalPlaces(faculty.getTotalPlaces())
                .budgetPlaces(faculty.getBudgetPlaces())
                .contractPlaces(faculty.getContractPlaces())
                .build();
        facultyService.saveFaculty(faculty2);
        return "redirect:/findFaculty";
    }
    @GetMapping("/showFormForUpdateFaculty/{facultyid}")
    public String updateFaculty(@PathVariable(value = "facultyid")long facultyid,Model model){
        Faculty faculty = facultyService.findByFacultyById(facultyid).get();

        model.addAttribute("faculty",faculty);
        return "facultyEdit";
    }

    @GetMapping("/deleteFaculty/{facultyid}")
    public String deleteFaculty(@PathVariable(value = "facultyid")long facultyid){
        facultyService.deleteFacultyById(facultyid);
        log.info("deleted faculty successful");
        return "redirect:/findFaculty";
    }

    @GetMapping("faculty/page/{pageNo}")
    public String findFacultyPaginated(
            @PathVariable (value = "pageNo") int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
                Model model){
        int pageSize = 5;

        Page<Faculty> page = facultyService.findFacultyPaginated(pageNo, pageSize,sortField,sortDir);
        List<Faculty> facultyList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",sortDir.equals("asc")?"desc":"asc");

        model.addAttribute("faculties", facultyList);
        return "findFaculty";
    }
}
