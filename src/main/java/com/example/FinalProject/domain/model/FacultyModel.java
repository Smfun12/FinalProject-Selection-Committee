package com.example.FinalProject.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FacultyModel {

    private long facultyid;

    private String title;

    private int budgetPlaces;

    private int contractPlaces;

    private int totalPlaces;

    private String firstSubject;

    private String secondSubject;

    private String thirdSubject;

    private Set<StudentModel> students;

    public FacultyModel(long facultyid, String title, int budgetPlaces, int contractPlaces, int totalPlaces,
                        String firstSubject, String secondSubject, String thirdSubject) {
        this.facultyid = facultyid;
        this.title = title;
        this.budgetPlaces = budgetPlaces;
        this.contractPlaces = contractPlaces;
        this.totalPlaces = totalPlaces;
        this.firstSubject = firstSubject;
        this.secondSubject = secondSubject;
        this.thirdSubject = thirdSubject;
    }
}
