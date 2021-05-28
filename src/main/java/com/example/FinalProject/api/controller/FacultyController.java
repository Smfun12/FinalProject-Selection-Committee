package com.example.FinalProject.api.controller;

import com.example.FinalProject.domain.model.FacultyModel;
import com.example.FinalProject.domain.service.FacultyService;
import com.example.FinalProject.utilities.FacultyPDFExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class for performing crud operations on Faculty entity
 */
@Slf4j
@RestController
public class FacultyController {

    private final FacultyService facultyService;

    ResourceBundleMessageSource messageSource;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("lang/res");
    }

//    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/faculties")
    public List<FacultyModel> facultyList() {
        return facultyService.getFaculties();
    }
    /**
     * Search faculty with filter
     * @param filter - search
     * @return page
     */
//    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping("/filterFaculty")
    public Object findFaculty(@RequestParam String filter) {
        List<FacultyModel> faculties;
        if (filter != null && filter.isEmpty()) {
            faculties = facultyService.getFaculties();
            log.info("Find faculty with filter: " + filter);
        } else {
            faculties = new LinkedList<>();
            try {
                FacultyModel faculty = facultyService.findByTitle(filter);
                faculties.add(faculty);
            }
            catch (NoSuchElementException e){
                return e.getMessage();
            }
        }
        return faculties;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/faculty/new")
    public FacultyModel addFaculty(
            @Valid FacultyModel faculty, BindingResult result) {
        if (result.hasErrors()) {
            return null;
        }
        if (faculty.getContractPlaces() + faculty.getBudgetPlaces() != faculty.getTotalPlaces()) {
            log.info("Budget and contract places should be equal total places");
            return null;
        }

        FacultyModel faculty2 = new FacultyModel(
                faculty.getFacultyid(),
                faculty.getTitle(),
                faculty.getTotalPlaces(),
                faculty.getBudgetPlaces(),
                faculty.getContractPlaces(),
                faculty.getFirstSubject(),
                faculty.getSecondSubject(),
                faculty.getThirdSubject()
        );
        try {
            facultyService.saveFaculty(faculty2);
        } catch (Exception e) {
            log.info("Faculty" + faculty + " exists");
            return null;
        }
        log.info("Added faculty successfully");
        return faculty;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/deleteFaculty/{facultyid}")
    public List<FacultyModel> deleteFaculty(@PathVariable(value = "facultyid") long facultyid) {
        facultyService.deleteFacultyById(facultyid);
        log.info("Deleted faculty successfully");
        return facultyService.getFaculties();
    }

    /**
     * Show students that made application on current faculty
     * @param facultyid - current faculty
     * @return studentList
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/studentList/{facultyid}")
    public Object studentListPage(@PathVariable(value = "facultyid") long facultyid) {
        try {
            FacultyModel faculty = facultyService.findByFacultyById(facultyid);
            log.info("Show students on current faculty");
            return faculty.getStudents();
        }
        catch (NoSuchElementException e){
            return e.getMessage();
        }
    }

    @GetMapping("/faculties/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=faculties_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<FacultyModel> facultyList = facultyService.getFaculties();

        FacultyPDFExporter exporter = new FacultyPDFExporter(facultyList);
        exporter.export();
        log.info("Exporting faculties to pdf successfully");
    }
}
