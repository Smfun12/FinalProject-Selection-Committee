package com.example.FinalProject.domain.service;

import com.example.FinalProject.domain.model.StudentModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Class search current user in session in db, and give corresponding authority
 */
@Service
public class StudentDetailsServiceImpl implements UserDetailsService {

    private final StudentService studentService;

    public StudentDetailsServiceImpl(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        StudentModel student = studentService.findByLogin(username);
        return new StudentDetailsImpl(student);
    }

}