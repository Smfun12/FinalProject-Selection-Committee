package com.example.FinalProject.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class FacultyModel {

    private long facultyid;

    private String title;

    private int budgetPlaces;

    private int contractPlaces;

    private int totalPlaces;

    private String firstSubject;

    private String secondSubject;

    private String thirdSubject;

    private Set<Long> students = new HashSet<>();
}
