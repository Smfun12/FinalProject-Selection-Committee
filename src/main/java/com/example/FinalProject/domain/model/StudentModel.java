package com.example.FinalProject.domain.model;

import com.example.FinalProject.pestistence.entity.Faculty;
import com.example.FinalProject.pestistence.entity.Roles;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class StudentModel {

    private long studentid;

    private String email;

    private String login;

    private String password;

    private String city;

    private String district;

    private String school;

    public boolean budget = true;

    private Set<String> faculties
            = new HashSet<>();

    private Set<Roles> rolesSet;

    private boolean enabled = true;

    private int firstGrade;

    private int secondGrade;

    private int thirdGrade;
}
