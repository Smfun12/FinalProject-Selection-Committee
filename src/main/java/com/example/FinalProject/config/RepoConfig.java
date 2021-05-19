package com.example.FinalProject.config;

import com.example.FinalProject.repository.StudentJDBCRepo;
import com.example.FinalProject.repository.StudentJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RepoConfig {

    @Autowired
    private StudentJpaRepo studentJpaRepo;

    @Autowired
    private StudentJDBCRepo studentJDBCRepo;

    @Bean
    @Primary
    public StudentJpaRepo getStudentJpaRepo() {
        return studentJpaRepo;
    }

    @Bean
    public StudentJDBCRepo getStudentJDBCRepo() {
        return studentJDBCRepo;
    }
}
