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


    @GetMapping("/student")
    public String studentList(Model model){
        model.addAttribute("students",studentService.getStudents());
        return "findStudent";
    }

    @GetMapping("/student/{student}")
    public String studentEdit(@PathVariable Student student, Model model){
        model.addAttribute("student",student);
        return "studentEdit";
    }

    @PostMapping("/student")
    public String studentSave(
            @RequestParam String login,
            Student student){
        student.setLogin(login);
        studentService.saveStudent(student);
        return "redirect:/findStudent";

    }
    @GetMapping("/student/{id}")
    public String deleteStudent(@PathVariable (value = "id") long id){
        studentService.deleteStudentById(id);
        return "redirect:/findStudent";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                Model model){
        int pageSize = 5;

        Page<Student> page = studentService.findPaginated(pageNo, pageSize);
        List<Student> studentList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("studentList", studentList);
        return "findStudent";
    }
}
