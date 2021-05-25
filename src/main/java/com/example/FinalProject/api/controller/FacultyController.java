package com.example.FinalProject.api.controller;

import com.example.FinalProject.domain.model.FacultyModel;
import com.example.FinalProject.domain.service.FacultyService;
import com.example.FinalProject.utilities.FacultyPDFExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class for performing crud operations on Faculty entity
 */
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class FacultyController {

    private FacultyService facultyService;

    ResourceBundleMessageSource messageSource;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    public FacultyController(){
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("lang/res");
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/faculties")
    public List<FacultyModel> facultyList() {
        return findFacultyPaginated(1, "title", "asc");
    }
    /**
     * Search faculty with filter
     * @param filter - search
     * @return page
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping("/filterFaculty")
    public List<FacultyModel> findFaculty(@RequestParam String filter) {
        List<FacultyModel> faculties;
        if (filter != null && filter.isEmpty()) {
            faculties = facultyService.getFaculties();
            log.info("Find faculty with filter: " + filter);
        } else {
            Optional<FacultyModel> faculty = facultyService.findByTitle(filter);
            faculties = new LinkedList<>();
            faculty.ifPresent(faculties::add);
        }
        return faculties;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/faculty/{faculty}")
    public FacultyModel facultyEdit(@PathVariable FacultyModel faculty) {
        log.info("Show edit page for current faculty");
        return faculty;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/faculty/page")
    public String addFacultyPage() {
        log.info("Show 'Add faculty' page");
        return "addFaculty";
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

        FacultyModel faculty2 = FacultyModel.builder()
                .title(faculty.getTitle())
                .totalPlaces(faculty.getTotalPlaces())
                .budgetPlaces(faculty.getBudgetPlaces())
                .contractPlaces(faculty.getContractPlaces())
                .firstSubject(faculty.getFirstSubject())
                .secondSubject(faculty.getSecondSubject())
                .thirdSubject(faculty.getThirdSubject())
                .build();
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
    @GetMapping("/showFormForUpdateFaculty/{facultyid}")
    public FacultyModel updateFaculty(@PathVariable(value = "facultyid") long facultyid) {
        Optional<FacultyModel> faculty = facultyService.findByFacultyById(facultyid);
        if (!faculty.isPresent()){
            return null;
        }
        log.info("Show faculty edit page");
        return faculty.get();
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
    public Set<Long> studentListPage(@PathVariable(value = "facultyid") long facultyid) {
        Optional<FacultyModel> faculty = facultyService.findByFacultyById(facultyid);
        if (!faculty.isPresent()){
            return null;
        }
        log.info("Show students on current faculty");
        return faculty.get().getStudents();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("faculty/page/{pageNo}")
    public List<FacultyModel> findFacultyPaginated(
            @PathVariable(value = "pageNo") int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir) {
        int pageSize = 5;

        Page<FacultyModel> page = facultyService.findFacultyPaginated(pageNo, pageSize, sortField, sortDir);
        List<FacultyModel> facultyList = page.getContent();

        log.info("Show faculty list page");
        return facultyList;
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
