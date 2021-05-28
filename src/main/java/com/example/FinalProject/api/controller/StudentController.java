package com.example.FinalProject.api.controller;

import com.example.FinalProject.domain.model.FacultyModel;
import com.example.FinalProject.domain.model.StudentModel;
import com.example.FinalProject.domain.service.FacultyService;
import com.example.FinalProject.domain.service.StudentService;
import com.example.FinalProject.utilities.StudentPDFExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller for student performs crud operations
 */

@Slf4j
@RestController
public class StudentController {

    private final StudentService studentService;

    private final FacultyService facultyService;

    public StudentController(StudentService studentService, FacultyService facultyService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/findStudent")
    public List<StudentModel> findStudent() {
        return studentService.getStudents();
    }

    /**
     * Search student with filter
     *
     * @param filter - search
     * @return page
     */
//    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/filterStudent")
    public Object find(@RequestParam String filter) {
        List<StudentModel> students;
        if (filter != null && filter.isEmpty()) {
            students = studentService.getStudents();
        } else {
            students = new LinkedList<>();
            try {
                StudentModel student = studentService.findByEmail(filter);
                students.add(student);
            }
            catch (NoSuchElementException e){
                return e.getMessage();
            }
        }
        log.info("Find student with filter: " + filter);
        return students;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/student/addStudent")
    public StudentModel studentSave(
            @ModelAttribute("student") StudentModel studentModel) {
        Optional<StudentModel> student = Optional.ofNullable(studentModel);
        student.ifPresent(studentService::saveStudent);
        log.info("Save student");
        return studentModel;

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/showFormForUpdate/{studentid}")
    public StudentModel updateStudent(@PathVariable(value = "studentid") long studentid) {
        StudentModel student = studentService.findStudentById(studentid);
        log.info("Show edit student page");
        return student;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/deleteStudent/{studentid}")
    public List<StudentModel> deleteStudent(@PathVariable(value = "studentid") long studentid) {
        studentService.deleteStudentById(studentid);
        log.info("Deleted student successfully");
        return studentService.getStudents();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/disableStudent/{studentid}")
    public StudentModel disableStudent(@PathVariable(value = "studentid") long studentid) {
        studentService.disableStudentById(studentid);
        log.info("Disable student successfully");
        return studentService.findStudentById(studentid);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/enableStudent/{studentid}")
    public StudentModel enableStudent(@PathVariable(value = "studentid") long studentid) {
        StudentModel student = studentService.findStudentById(studentid);

        student.getFaculties().clear();
        studentService.saveStudent(student);
        studentService.enableStudentById(studentid);
        log.info("Enable student successfully");
        return student;
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
        StudentModel student = studentService.findByLogin(currentPrincipalName);
        student.setFirstGrade(Integer.parseInt(firstSubject));
        student.setSecondGrade(Integer.parseInt(secondSubject));
        student.setThirdGrade(Integer.parseInt(thirdSubject));
        student.getFaculties().add(facultyService.findByFacultyById(facultyid));
        studentService.saveStudent(student);
        log.info("StudentModel applied on faculty successfully");
        return student;
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
        StudentModel student = studentService.findStudentById(studentid);

        log.info("Show user profile page");
        return student;
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
        StudentModel student = studentService.findStudentById(studentid);
        FacultyModel faculty = facultyService.findByFacultyById(Long.parseLong(filter));

        student.getFaculties().clear();
        student.getFaculties().add(faculty);
        student.setEnabled(false);
        studentService.saveStudent(student);
        log.info("Admin submitted faculty for student successfully");
        return student;
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

        List<StudentModel> studentList = studentService.getStudents();

        StudentPDFExporter exporter = new StudentPDFExporter(studentList);
        exporter.export();
        log.info("Export students to pdf");

    }
}
