package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Faculty;
import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.StudentRepository;
import com.example.FinalProject.services.FacultyService;
import com.example.FinalProject.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Slf4j
@Controller
@RequestMapping()
@PreAuthorize("hasAuthority('ADMIN')")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private FacultyService facultyService;

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/student/{student}")
    public String studentEdit(@PathVariable Student student, Model model){
        model.addAttribute("student",student);
        return "studentEdit";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/student/addStudent")
    public String studentSave(
            @ModelAttribute("student") Student student){
        studentService.saveStudent(student);
        return "redirect:/findStudent";

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/applyForFaculty/{facultyid}")
    public String applyForFaculty(@PathVariable(value = "facultyid")long facultyid,
                                  Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Student student = studentService.findByLogin(currentPrincipalName).get();
        if(!student.isEnabled()){
            log.info("student already applied");
            model.addAttribute("applied","You are already applied");
            return "redirect:/findFaculty";
        }
        student.getFaculties().add(facultyService.findByFacultyById(facultyid).get());
        studentService.saveStudent(student);
        return "redirect:/findFaculty";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/showFormForUpdate/{studentid}")
    public String updateStudent(@PathVariable(value = "studentid")long studentid,Model model){
        Student student = studentService.findStudentById(studentid).get();

        model.addAttribute("student",student);
        return "studentEdit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deleteStudent/{studentid}")
    public String deleteStudent(@PathVariable(value = "studentid")long studentid,Model model){
        studentService.deleteStudentById(studentid);
        return "redirect:/findStudent";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/disableStudent/{studentid}")
    public String disableStudent(@PathVariable(value = "studentid")long studentid,Model model){
        studentService.disableStudentById(studentid);
        return "redirect:/findStudent";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/enableStudent/{studentid}")
    public String enableStudent(@PathVariable(value = "studentid")long studentid){
        studentService.enableStudentById(studentid);
        return "redirect:/findStudent";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userProfile/{studentid}")
    public String userProfile(@PathVariable(value = "studentid")long studentid,Model model){
        Student student = studentService.findStudentById(studentid).get();
        model.addAttribute("student",student);
        model.addAttribute("login",student.getLogin());
        model.addAttribute("email",student.getEmail());
        return "userProfile";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/userProfile")
    public String userProfilePage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Student student = studentService.findByLogin(currentPrincipalName).get();
        model.addAttribute("student",student);
        model.addAttribute("login",student.getLogin());
        model.addAttribute("email",student.getEmail());
        return "userProfile";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/submitFaculty/{studentid}")
    public String submitFaculty(@PathVariable(value = "studentid")long studentid,
                                @RequestParam String filter){
        Student student = studentService.findStudentById(studentid).get();
        Faculty faculty = facultyService.findByFacultyById(Long.parseLong(filter)).get();
        student.getFaculties().clear();
        student.getFaculties().add(faculty);
        student.setEnabled(false);
        studentService.saveStudent(student);
        return "redirect:/findStudent";
    }

    @PreAuthorize("hasRole('ADMIN')")
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
