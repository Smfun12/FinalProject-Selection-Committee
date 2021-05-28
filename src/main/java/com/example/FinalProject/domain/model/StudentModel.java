package com.example.FinalProject.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class StudentModel implements Comparable<StudentModel>{

    private long studentid;

    private String email;

    private String login;

    private String password;

    private String city;

    private String district;

    private String school;

    public boolean budget;

    private Set<FacultyModel> faculties;

    private Set<RolesModel> rolesSet;

    private boolean enabled;

    private int firstGrade;

    private int secondGrade;

    private int thirdGrade;

    public StudentModel(long studentid, String email, String login, String password, String city, String district,
                        String school,Set<RolesModel> rolesSet,  boolean budget) {
        this.studentid = studentid;
        this.email = email;
        this.login = login;
        this.password = password;
        this.city = city;
        this.district = district;
        this.school = school;
        this.budget = budget;
        this.rolesSet = rolesSet;
    }

    @Override
    public int compareTo(StudentModel o) {
        double avgThisGrade = (this.firstGrade + this.secondGrade + this.thirdGrade) / 3.0;
        double avgThatGrade = (o.firstGrade + o.secondGrade + o.thirdGrade) / 3.0;
        return -Double.compare(avgThisGrade,avgThatGrade);
    }
}
