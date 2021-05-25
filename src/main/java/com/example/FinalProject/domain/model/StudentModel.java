package com.example.FinalProject.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class StudentModel {

    private long studentid;

    private String email;

    private String login;

    private String password;

    private String city;

    private String district;

    private String school;

    public boolean budget;

    private Set<String> faculties;

    private Set<RolesModel> rolesSet;

    private boolean enabled;

    private int firstGrade;

    private int secondGrade;

    private int thirdGrade;
}
