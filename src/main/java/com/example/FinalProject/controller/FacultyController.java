package com.example.FinalProject.controller;

import com.example.FinalProject.entities.Faculty;
import com.example.FinalProject.services.FacultyService;
import com.example.FinalProject.utilities.FacultyPDFExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Class for performing crud operations on Faculty entity
 */
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    ResourceBundleMessageSource messageSource;

    public FacultyController(){
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("lang/res");
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/findFaculty")
    public String facultyList(Model model) {
        return findFacultyPaginated(1, "title", "asc", model);
    }
    /**
     * Search faculty with filter
     * @param filter - search
     * @param model - add attribute to view
     * @return page
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping("/filterFaculty")
    public String findFaculty(@RequestParam String filter, Model model) {
        List<Faculty> faculties;
        if (filter != null && filter.isEmpty()) {
            faculties = facultyService.getFaculties();
            model.addAttribute("faculties", faculties);
        } else {
            Optional<Faculty> faculty = facultyService.findByTitle(filter);
            faculty.ifPresent(value -> model.addAttribute("faculties", value));
        }
        log.info("Find faculty with filter: " + filter);
        return "findFaculty";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/faculty/saveFaculty")
    public String saveFaculty(
            @Valid Faculty faculty, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", messageSource.getMessage("faculty_error",null,
                    LocaleContextHolder.getLocale()));
            return "facultyEdit";
        }
        if (faculty.getContractPlaces() + faculty.getBudgetPlaces() != faculty.getTotalPlaces()) {
            model.addAttribute("error", messageSource.getMessage("totalPlaces_error", null,
                    LocaleContextHolder.getLocale())
            );
            return "facultyEdit";
        }
        facultyService.saveFaculty(faculty);
        log.info("add faculty successful");
        return "redirect:/findFaculty";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/faculty/{faculty}")
    public String facultyEdit(
                              @PathVariable Faculty faculty, Model model) {

        model.addAttribute("faculty", faculty);
        log.info("Show edit page for current faculty");
        return "facultyEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/faculty/addFaculty")
    public String addFacultyPage() {
        log.info("Show 'Add faculty' page");
        return "addFaculty";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/faculty/addFaculty")
    public String addFaculty(
            @Valid Faculty faculty, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", messageSource.getMessage("faculty_error",null,
                    LocaleContextHolder.getLocale()));
            return "addFaculty";
        }
        if (faculty.getContractPlaces() + faculty.getBudgetPlaces() != faculty.getTotalPlaces()) {
            model.addAttribute("error", "Budget and contract places should be equal total places");
            return "addFaculty";
        }

        Faculty faculty2 = Faculty.builder()
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
            model.addAttribute("error", messageSource.getMessage("faculty_exists",null,
                    LocaleContextHolder.getLocale()));
            log.info("Faculty" + faculty + " exists");
            return "addFaculty";
        }
        log.info("Added faculty successfully");
        return "redirect:/findFaculty";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/showFormForUpdateFaculty/{facultyid}")
    public String updateFaculty(@PathVariable(value = "facultyid") long facultyid, Model model) {
        Optional<Faculty> faculty = facultyService.findByFacultyById(facultyid);
        if (!faculty.isPresent()){
            return "findFaculty";
        }
        model.addAttribute("faculty", faculty.get());
        log.info("Show faculty edit page");
        return "facultyEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/deleteFaculty/{facultyid}")
    public String deleteFaculty(@PathVariable(value = "facultyid") long facultyid) {
        facultyService.deleteFacultyById(facultyid);
        log.info("Deleted faculty successfully");
        return "redirect:/findFaculty";
    }

    /**
     * Show students that made application on current faculty
     * @param facultyid - current faculty
     * @param model - view
     * @return studentList
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/studentList/{facultyid}")
    public String studentListPage(@PathVariable(value = "facultyid") long facultyid, Model model) {
        Optional<Faculty> faculty = facultyService.findByFacultyById(facultyid);
        if (!faculty.isPresent()){
            return "findFaculty";
        }
        model.addAttribute("students", faculty.get().getStudents());
        model.addAttribute("facultyid", facultyid);
        log.info("Show students on current faculty");
        return "studentList";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("faculty/page/{pageNo}")
    public String findFacultyPaginated(
            @PathVariable(value = "pageNo") int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model) {
        int pageSize = 5;

        Page<Faculty> page = facultyService.findFacultyPaginated(pageNo, pageSize, sortField, sortDir);
        List<Faculty> facultyList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("faculties", facultyList);
        log.info("Show faculty list page");
        return "findFaculty";
    }

    @GetMapping("/faculties/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=faculties_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Faculty> facultyList = facultyService.getFaculties();

        FacultyPDFExporter exporter = new FacultyPDFExporter(facultyList);
        exporter.export();
        log.info("Exporting faculties to pdf successfully");
    }
}
