package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.StudentRepository;
import com.example.FinalProject.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping()
@PreAuthorize("hasAuthority('ADMIN')")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findStudent")
    public String findStudent(Model model) {
        return findPaginated(1,"studentid","asc",model);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/student")
    public String studentList(Model model){
        return findPaginated(1,"studentid","asc",model);
    }

    @GetMapping("/student/{student}")
    public String studentEdit(@PathVariable Student student, Model model){
        model.addAttribute("student",student);
        return "studentEdit";
    }

    @PostMapping("/student/addStudent")
    public String studentSave(
            @ModelAttribute("student") Student student){
        studentService.saveStudent(student);
        return "redirect:/findStudent";

    }

    @GetMapping("/showFormForUpdate/{studentid}")
    public String updateStudent(@PathVariable(value = "studentid")long studentid,Model model){
        Student student = studentService.findStudentById(studentid).get();

        model.addAttribute("student",student);
        return "studentEdit";
    }

    @GetMapping("/deleteStudent/{studentid}")
    public String deleteStudent(@PathVariable(value = "studentid")long studentid,Model model){
        studentService.deleteStudentById(studentid);
        return "redirect:/findStudent";
    }
    @GetMapping("/disableStudent/{studentid}")
    public String disableStudent(@PathVariable(value = "studentid")long studentid,Model model){
        studentService.disableStudentById(studentid);
        return "redirect:/findStudent";
    }

    @GetMapping("/enableStudent/{studentid}")
    public String enableStudent(@PathVariable(value = "studentid")long studentid,Model model){
        studentService.enableStudentById(studentid);
        return "redirect:/findStudent";
    }
    @GetMapping("/student/page/{pageNo}")
    public String findPaginated(
            @PathVariable (value = "pageNo") int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model){
        int pageSize = 5;

        Page<Student> page = studentService.findPaginated(pageNo, pageSize,sortField,sortDir);
        List<Student> studentList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",sortDir.equals("asc")?"desc":"asc");

        model.addAttribute("students", studentList);
        return "findStudent";
    }
}
