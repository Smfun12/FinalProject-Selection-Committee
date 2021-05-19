package com.example.FinalProject.controller;

import com.example.FinalProject.entities.models.Faculty;
import com.example.FinalProject.entities.models.Student;
import com.example.FinalProject.services.FacultyService;
import com.example.FinalProject.services.StudentService;
import com.example.FinalProject.utilities.StudentPDFExporter;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for student performs crud operations
 */

@Slf4j
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class StudentController {

    private final StudentService studentService;

    private final FacultyService facultyService;

    @Autowired
    public StudentController(StudentService studentService, FacultyService facultyService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/findStudent")
    public String findStudent(Model model) {
        return findPaginated(1, "studentid", "asc", model);
    }

    /**
     * Search student with filter
     *
     * @param filter - search
     * @param model  - add attribute to view
     * @return page
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/filterStudent")
    public String find(@RequestParam String filter, Model model) {
        List<Student> students;
        if (filter != null && filter.isEmpty()) {
            students = studentService.getStudents();
        } else {
            Optional<Student> student = studentService.findByEmail(filter);
            students = new LinkedList<>();
            students.add(student.get());
        }
        model.addAttribute("students", students);
        log.info("Find student with filter: " + filter);
        return "findStudent";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/filterStudent",headers = "")
    public String find(@RequestParam String filter, @RequestHeader(value = "jdbc", required = false) String header, Model model) {
        List<Student> students;
        if (filter != null && filter.isEmpty()) {
            students = studentService.getStudents();
        } else {
            Optional<Student> student = studentService.findByEmail(filter);
            students = new LinkedList<>();
            students.add(student.get());
        }
        model.addAttribute("students", students);
        log.info("Find student with filter: " + filter);
        return "findStudent";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/student/addStudent")
    public String studentSave(
            @ModelAttribute("student") Student student) {
        studentService.saveStudent(student);
        log.info("Save student");
        return "redirect:/findStudent";

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/showFormForUpdate/{studentid}")
    public String updateStudent(@PathVariable(value = "studentid") long studentid, Model model) {
        Optional<Student> student = studentService.findStudentById(studentid);
        if (!student.isPresent()) {
            return "findStudent";
        }
        model.addAttribute("student", student.get());
        log.info("Show edit student page");
        return "studentEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/deleteStudent/{studentid}")
    public String deleteStudent(@PathVariable(value = "studentid") long studentid) {
        studentService.deleteStudentById(studentid);
        log.info("Deleted student successfully");
        return "redirect:/findStudent";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/disableStudent/{studentid}")
    public String disableStudent(@PathVariable(value = "studentid") long studentid) {
        studentService.disableStudentById(studentid);
        log.info("Disable student successfully");
        return "redirect:/findStudent";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/enableStudent/{studentid}")
    public String enableStudent(@PathVariable(value = "studentid") long studentid) {
        Optional<Student> student = studentService.findStudentById(studentid);
        if (!student.isPresent()) {
            return "findStudent";
        }
        student.get().getFaculties().clear();
        studentService.saveStudent(student.get());
        studentService.enableStudentById(studentid);
        log.info("Enable student successfully");
        return "redirect:/findStudent";
    }

    /**
     * Show to student page for applying on faculty,
     * if student is already applied by admin on faculty, he is disabled
     * and cannot apply anymore
     *
     * @param facultyid          - selected faculty
     * @param redirectAttributes - show error
     * @param model              - add attribute to view
     * @return page
     */
    @Transactional
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/applyForFaculty/{facultyid}")
    public String applyOnFacultyPage(@PathVariable(value = "facultyid") long facultyid,
                                     RedirectAttributes redirectAttributes, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<Student> student = studentService.findByLogin(currentPrincipalName);
        if (!student.isPresent()) {
            return "mainPage";
        }
        if (!student.get().isEnabled()) {
            log.info("student already applied");
            redirectAttributes.addFlashAttribute("applied", "You are already applied");
            return "redirect:/findFaculty";
        }
        model.addAttribute("faculty", facultyService.findByFacultyById(facultyid).get());
        log.info("Show apply on faculty page");
        return "applyOnFaculty";
    }

    /**
     * Accepts input fields from 'Apply on faculty' page
     * Additionaly check the range of grades (between 100 and 200 is valid)
     *
     * @param facultyid     - selected faculty
     * @param firstSubject  - grade of 1 subject
     * @param secondSubject - grade of 1 subject
     * @param thirdSubject  - grade of 1 subject
     * @param model         - add error on view
     * @return findFaculty page, if everything is ok
     */
    @Transactional
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/applyForFaculty/{facultyid}")
    public String applyOnFaculty(@PathVariable(value = "facultyid") long facultyid,
                                 @RequestParam("firstSubject") String firstSubject,
                                 @RequestParam("secondSubject") String secondSubject,
                                 @RequestParam("thirdSubject") String thirdSubject,
                                 Model model) {
        try {
            int firstGrade = Integer.parseInt(firstSubject);
            int secondGrade = Integer.parseInt(secondSubject);
            int thirdGrade = Integer.parseInt(thirdSubject);
            if (firstGrade < 100 || firstGrade > 200 ||
                    secondGrade < 100 || secondGrade > 200 ||
                    thirdGrade < 100 || thirdGrade > 200) {
                model.addAttribute("faculty", facultyService.findByFacultyById(facultyid).get());
                model.addAttribute("error", "Grade should be in range 100 and 200");
                log.info("Wrong grades");
                return "applyOnFaculty";
            }
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Grade should be in range 100 and 200");
            log.info("Wrong grades");
            return "applyOnFaculty";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<Student> student = studentService.findByLogin(currentPrincipalName);
        if (!student.isPresent()) {
            return "mainPage";
        }
        student.get().setFirstGrade(Integer.parseInt(firstSubject));
        student.get().setSecondGrade(Integer.parseInt(secondSubject));
        student.get().setThirdGrade(Integer.parseInt(thirdSubject));
        student.get().getFaculties().add(facultyService.findByFacultyById(facultyid).get());
        studentService.saveStudent(student.get());
        log.info("Student applied on faculty successfully");
        return "redirect:/findFaculty";
    }

    /**
     * This method is called, when admin click on id on studentList page
     *
     * @param studentid - selected student id
     * @param model     - view
     * @return user profile page
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/userProfile/{studentid}")
    public String userProfile(@PathVariable(value = "studentid") long studentid, Model model) {
        Optional<Student> student = studentService.findStudentById(studentid);
        if (!student.isPresent()) {
            return "findStudent";
        }
        model.addAttribute("student", student.get());
        if (student.get().getLogin().equals("admin")) {
            model.addAttribute("finalize", "Finalize");
        }
        log.info("Show user profile page");
        return "userProfile";
    }

    /**
     * This method is called, when user or admin click in navbar on own login
     *
     * @param model - view
     * @return user profile page
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/userProfile")
    public String userProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<Student> student = studentService.findByLogin(currentPrincipalName);
        if (!student.isPresent()) {
            return "mainPage";
        }
        model.addAttribute("student", student.get());
        if (student.get().getLogin().equals("admin")) {
            model.addAttribute("finalize", "Finalize");
        }
        log.info("Show user profile page");
        return "userProfile";
    }

    /**
     * Submitting faculty for student
     *
     * @param studentid - selected student
     * @param filter    - Faculty title
     * @return student list page
     */
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/submitFaculty/{studentid}")
    public String submitFaculty(@PathVariable(value = "studentid") long studentid,
                                @RequestParam String filter) {
        Optional<Student> student = studentService.findStudentById(studentid);
        Optional<Faculty> faculty = facultyService.findByFacultyById(Long.parseLong(filter));
        if (!student.isPresent() || !faculty.isPresent()) {
            return "findFaculty";
        }
        student.get().getFaculties().clear();
        student.get().getFaculties().add(faculty.get());
        student.get().setEnabled(false);
        studentService.saveStudent(student.get());
        log.info("Admin submitted faculty for student successfully");
        return "redirect:/findStudent";
    }

    /**
     * Show studentList page with sorting
     *
     * @param pageNo    - current page
     * @param sortField - sort by
     * @param sortDir   - sort direction (asc of desc)
     * @param model     - view
     * @return studentList page with sorting
     */
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
        log.info("Show student list with sorting");
        return "findStudent";
    }

    /**
     * Save pdf file to root directory of this project
     *
     * @throws IOException - exception
     */
    @GetMapping("/students/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=students_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Student> studentList = studentService.getStudents();

        StudentPDFExporter exporter = new StudentPDFExporter(studentList);
        exporter.export();
        log.info("Export students to pdf");

    }
}
