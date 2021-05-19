package com.example.FinalProject.controller;

import com.example.FinalProject.entities.models.Roles;
import com.example.FinalProject.entities.models.Student;
import com.example.FinalProject.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;

@Controller
@Slf4j
public class RegistrationController {

    private StudentService studentService;

    private PasswordEncoder passwordEncoder;

    ResourceBundleMessageSource messageSource;

    @Autowired
    public RegistrationController(StudentService studentService, PasswordEncoder passwordEncoder) {
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
    }

    public RegistrationController(){
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("lang/res");
    }

    @GetMapping("/registration")
    public String registration() {
        log.info("Show registration page");
        return "registration";
    }

    @PostMapping("/registration")
    public String addStudent(@Valid Student student, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", messageSource.getMessage("user_error",
                    null, LocaleContextHolder.getLocale()));
            log.info(result.getAllErrors().toString());
            return "registration";
        }
        Student student2 = Student.builder().
                email(student.getEmail())
                .login(student.getLogin())
                .password(passwordEncoder.encode(student.getPassword()))
                .city(student.getCity())
                .district(student.getDistrict())
                .school(student.getSchool())
                .rolesSet(Collections.singleton(Roles.USER))
                .budget(false)
                .build();
        log.info(student2.toString());
        try {
            studentService.saveStudent(student2);
        }
        catch (Exception e){
            model.addAttribute("error", messageSource.getMessage("user_exists",null,
                    LocaleContextHolder.getLocale()));
            log.info("Student" + student + " exists");
            return "registration";
        }
        log.info("Registered student successfully");
        return "redirect:/login";
    }
}
