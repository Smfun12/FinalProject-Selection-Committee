package com.example.FinalProject.services;

import com.example.FinalProject.entities.Student;
import com.example.FinalProject.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Class search current user in session in db, and give corresponding authority
 */
@Service
public class StudentDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByLogin(username);

        if (!student.isPresent()) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new StudentDetailsImpl(student.get());
    }

}