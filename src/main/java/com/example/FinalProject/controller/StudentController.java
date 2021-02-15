package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Faculty;
import com.example.FinalProject.entities.Student;
import com.example.FinalProject.services.FacultyService;
import com.example.FinalProject.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping()
@PreAuthorize("hasAuthority('ADMIN')")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private FacultyService facultyService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/findStudent")
    public String findStudent(Model model) {
        return findPaginated(1, "studentid", "asc", model);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/student")
    public String studentList(Model model) {
        return findPaginated(1, "studentid", "asc", model);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/student/{student}")
    public String studentEdit(@PathVariable Student student, Model model) {
        model.addAttribute("student", student);
        return "studentEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/student/addStudent")
    public String studentSave(
            @ModelAttribute("student") Student student) {
        studentService.saveStudent(student);
        return "redirect:/findStudent";

    }

    @Transactional
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/applyForFaculty/{facultyid}")
    public String applyForFaculty(@PathVariable(value = "facultyid") long facultyid,
                                  RedirectAttributes redirectAttributes, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Student student = studentService.findByLogin(currentPrincipalName).get();
        if (!student.isEnabled()) {
            log.info("student already applied");
            redirectAttributes.addFlashAttribute("applied", "You are already applied");
            return "redirect:/findFaculty";
        }
        model.addAttribute("faculty", facultyService.findByFacultyById(facultyid).get());
        return "applyOnFaculty";
    }

    @Transactional
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/applyForFaculty/{facultyid}")
    public String applyOnFaculty(@PathVariable(value = "facultyid") long facultyid,
                                 @RequestParam("firstSubject") int firstSubject,
                                 @RequestParam("secondSubject") int secondSubject,
                                 @RequestParam("thirdSubject") int thirdSubject) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Student student = studentService.findByLogin(currentPrincipalName).get();
        student.setFirstGrade(firstSubject);
        student.setSecondGrade(secondSubject);
        student.setThirdGrade(thirdSubject);
        student.getFaculties().add(facultyService.findByFacultyById(facultyid).get());
        studentService.saveStudent(student);
        return "redirect:/findFaculty";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/showFormForUpdate/{studentid}")
    public String updateStudent(@PathVariable(value = "studentid") long studentid, Model model) {
        Student student = studentService.findStudentById(studentid).get();

        model.addAttribute("student", student);
        return "studentEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/deleteStudent/{studentid}")
    public String deleteStudent(@PathVariable(value = "studentid") long studentid) {
        studentService.deleteStudentById(studentid);
        return "redirect:/findStudent";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/disableStudent/{studentid}")
    public String disableStudent(@PathVariable(value = "studentid") long studentid) {
        studentService.disableStudentById(studentid);
        return "redirect:/findStudent";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/enableStudent/{studentid}")
    public String enableStudent(@PathVariable(value = "studentid") long studentid) {
        Student student = studentService.findStudentById(studentid).get();
        student.getFaculties().clear();
        studentService.saveStudent(student);
        studentService.enableStudentById(studentid);
        return "redirect:/findStudent";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/userProfile/{studentid}")
    public String userProfile(@PathVariable(value = "studentid") long studentid, Model model) {
        Student student = studentService.findStudentById(studentid).get();
        model.addAttribute("student", student);
        model.addAttribute("login", student.getLogin());
        model.addAttribute("email", student.getEmail());
        model.addAttribute("firstGrade", student.getFirstGrade());
        model.addAttribute("secondGrade", student.getSecondGrade());
        model.addAttribute("thirdGrade", student.getThirdGrade());
        return "userProfile";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/userProfile")
    public String userProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Student student = studentService.findByLogin(currentPrincipalName).get();
        model.addAttribute("student", student);
        model.addAttribute("login", student.getLogin());
        model.addAttribute("email", student.getEmail());
        model.addAttribute("firstGrade", student.getFirstGrade());
        model.addAttribute("secondGrade", student.getSecondGrade());
        model.addAttribute("thirdGrade", student.getThirdGrade());
        return "userProfile";
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/submitFaculty/{studentid}")
    public String submitFaculty(@PathVariable(value = "studentid") long studentid,
                                @RequestParam String filter) {
        Student student = studentService.findStudentById(studentid).get();
        Faculty faculty = facultyService.findByFacultyById(Long.parseLong(filter)).get();
        student.getFaculties().clear();
        student.getFaculties().add(faculty);
        student.setEnabled(false);
        studentService.saveStudent(student);
        return "redirect:/findStudent";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/student/page/{pageNo}")
    public String findPaginated(
            @PathVariable(value = "pageNo") int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model) {
        int pageSize = 5;

        Page<Student> page = studentService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Student> studentList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("students", studentList);
        return "findStudent";
    }
}
