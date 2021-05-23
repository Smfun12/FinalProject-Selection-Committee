package com.example.FinalProject.api.controller;

import com.example.FinalProject.api.mapper.StudentMapper;
import com.example.FinalProject.domain.model.StudentModel;
import com.example.FinalProject.domain.service.FacultyService;
import com.example.FinalProject.domain.service.StudentService;
import com.example.FinalProject.pestistence.entity.Faculty;
import com.example.FinalProject.pestistence.entity.Student;
import com.example.FinalProject.utilities.StudentPDFExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
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
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
public class StudentController {

    private final StudentService studentService;

    private final FacultyService facultyService;

    public StudentController(StudentService studentService, FacultyService facultyService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/findStudent")
    public List<Student> findStudent() {
        return findPaginated(1, "studentid", "asc");
    }

    /**
     * Search student with filter
     *
     * @param filter - search
     * @return page
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/filterStudent")
    public List<Student> find(@RequestParam String filter) {
        List<Student> students;
        if (filter != null && filter.isEmpty()) {
            students = studentService.getStudents();
        } else {
            Optional<Student> student = studentService.findByEmail(filter);
            students = new LinkedList<>();
            student.ifPresent(students::add);
        }
        log.info("Find student with filter: " + filter);
        return students;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/student/addStudent")
    public StudentModel studentSave(
            @ModelAttribute("student") StudentModel studentModel) {
        Optional<Student> student = StudentMapper.INSTANCE.studentModelTolStudent(studentModel);
        student.ifPresent(studentService::saveStudent);
        log.info("Save student");
        return studentModel;

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/showFormForUpdate/{studentid}")
    public StudentModel updateStudent(@PathVariable(value = "studentid") long studentid) {
        Optional<Student> student = studentService.findStudentById(studentid);
        if (!student.isPresent()) {
            return null;
        }
        log.info("Show edit student page");
        return StudentMapper.INSTANCE.studentToModelStudent(student.get()).get();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/deleteStudent/{studentid}")
    public List<Student> deleteStudent(@PathVariable(value = "studentid") long studentid) {
        studentService.deleteStudentById(studentid);
        log.info("Deleted student successfully");
        return studentService.getStudents();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/disableStudent/{studentid}")
    public StudentModel disableStudent(@PathVariable(value = "studentid") long studentid) {
        studentService.disableStudentById(studentid);
        log.info("Disable student successfully");
        return StudentMapper.INSTANCE.studentToModelStudent(studentService.findStudentById(studentid).get()).get();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/enableStudent/{studentid}")
    public StudentModel enableStudent(@PathVariable(value = "studentid") long studentid) {
        Optional<StudentModel> student = StudentMapper.INSTANCE.studentToModelStudent(studentService.findStudentById(studentid).get());
        if (!student.isPresent()) {
            return null;
        }
        student.get().getFaculties().clear();
        studentService.saveStudent(StudentMapper.INSTANCE.studentModelTolStudent(student.get()).get());
        studentService.enableStudentById(studentid);
        log.info("Enable student successfully");
        return student.get();
    }

    /**
     * Show to student page for applying on faculty,
     * if student is already applied by admin on faculty, he is disabled
     * and cannot apply anymore
     *
     * @param facultyid          - selected faculty
     * @param redirectAttributes - show error
     * @return page
     */
    @Transactional
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/applyForFaculty/{facultyid}")
    public String applyOnFacultyPage(@PathVariable(value = "facultyid") long facultyid,
                                     RedirectAttributes redirectAttributes) {
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
     * @return findFaculty page, if everything is ok
     */
    @Transactional
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/applyForFaculty/{facultyid}")
    public StudentModel applyOnFaculty(@PathVariable(value = "facultyid") long facultyid,
                                  @RequestParam("firstSubject") String firstSubject,
                                  @RequestParam("secondSubject") String secondSubject,
                                  @RequestParam("thirdSubject") String thirdSubject) {
        try {
            int firstGrade = Integer.parseInt(firstSubject);
            int secondGrade = Integer.parseInt(secondSubject);
            int thirdGrade = Integer.parseInt(thirdSubject);
            if (firstGrade < 100 || firstGrade > 200 ||
                    secondGrade < 100 || secondGrade > 200 ||
                    thirdGrade < 100 || thirdGrade > 200) {
                log.info("Wrong grades");
                return null;
            }
        } catch (NumberFormatException e) {
            log.info("Wrong grades");
            return null;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<StudentModel> student = StudentMapper.INSTANCE.studentToModelStudent(studentService.findByLogin(currentPrincipalName).get());
        if (!student.isPresent()) {
            return null;
        }
        student.get().setFirstGrade(Integer.parseInt(firstSubject));
        student.get().setSecondGrade(Integer.parseInt(secondSubject));
        student.get().setThirdGrade(Integer.parseInt(thirdSubject));
        student.get().getFaculties().add(facultyService.findByFacultyById(facultyid).get().getTitle());
        studentService.saveStudent(StudentMapper.INSTANCE.studentModelTolStudent(student.get()).get());
        log.info("Student applied on faculty successfully");
        return student.get();
    }

    /**
     * This method is called, when admin click on id on studentList page
     *
     * @param studentid - selected student id
     * @return user profile page
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/userProfile/{studentid}")
    public StudentModel userProfile(@PathVariable(value = "studentid") long studentid) {
        Optional<StudentModel> student = StudentMapper.INSTANCE.studentToModelStudent(studentService.findStudentById(studentid).get());
        if (!student.isPresent()) {
            return null;
        }
        log.info("Show user profile page");
        return student.get();
    }

    /**
     * This method is called, when user or admin click in navbar on own login
     *
     * @return user profile page
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/userProfile")
    public String userProfilePage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<Student> student = studentService.findByLogin(currentPrincipalName);
        if (!student.isPresent()) {
            return "mainPage";
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
    public StudentModel submitFaculty(@PathVariable(value = "studentid") long studentid,
                                 @RequestParam String filter) {
        Optional<StudentModel> student = StudentMapper.INSTANCE.studentToModelStudent(studentService.findStudentById(studentid).get());
        Optional<Faculty> faculty = facultyService.findByFacultyById(Long.parseLong(filter));
        if (!student.isPresent() || !faculty.isPresent()) {
            return null;
        }
        student.get().getFaculties().clear();
        student.get().getFaculties().add(faculty.get().getTitle());
        student.get().setEnabled(false);
        studentService.saveStudent(StudentMapper.INSTANCE.studentModelTolStudent(student.get()).get());
        log.info("Admin submitted faculty for student successfully");
        return student.get();
    }

    /**
     * Show studentList page with sorting
     *
     * @param pageNo    - current page
     * @param sortField - sort by
     * @param sortDir   - sort direction (asc of desc)
     * @return studentList page with sorting
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/student/page/{pageNo}")
    public List<Student> findPaginated(
            @PathVariable(value = "pageNo") int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir) {
        int pageSize = 5;

        Page<Student> page = studentService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Student> studentList = page.getContent();

        log.info("Show student list with sorting");
        return studentList;
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
