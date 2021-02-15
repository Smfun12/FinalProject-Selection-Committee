package com.example.FinalProject.entities;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long facultyid;

    @NotBlank
    @Length(min = 3)
    @Column(name = "title")
    private String title;


    @Positive
    @Column(name = "budgetPlaces")
    private int budgetPlaces;

    @Positive
    @Column(name = "contractPlaces")
    private int contractPlaces;

    @Positive
    @Column(name = "totalPlaces")
    private int totalPlaces;

    private String firstSubject;

    private String secondSubject;

    public String getFirstSubject() {
        return firstSubject;
    }

    public void setFirstSubject(String firstSubject) {
        this.firstSubject = firstSubject;
    }

    public String getSecondSubject() {
        return secondSubject;
    }

    public void setSecondSubject(String secondSubject) {
        this.secondSubject = secondSubject;
    }

    public String getThirdSubject() {
        return thirdSubject;
    }

    public void setThirdSubject(String thirdSubject) {
        this.thirdSubject = thirdSubject;
    }

    private String thirdSubject;

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @ManyToMany(mappedBy = "faculties", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    @Builder
    public Faculty(@NotBlank @Length(min = 10) String title, @Positive int totalPlaces,
                   @Positive int budgetPlaces, @Positive int contractPlaces, @NotBlank String firstSubject,
                   @NotBlank String secondSubject, @NotBlank String thirdSubject) {
        this.title = title;
        this.totalPlaces = totalPlaces;
        this.budgetPlaces = budgetPlaces;
        this.contractPlaces = contractPlaces;
        this.firstSubject = firstSubject;
        this.secondSubject = secondSubject;
        this.thirdSubject = thirdSubject;
    }

    public Faculty() {

    }

    public long getFacultyid() {
        return facultyid;
    }

    public void setFacultyid(long faculty_id) {
        this.facultyid = faculty_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalPlaces() {
        return totalPlaces;
    }

    public void setTotalPlaces(int totalPlaces) {
        this.totalPlaces = totalPlaces;
    }

    public int getBudgetPlaces() {
        return budgetPlaces;
    }

    public void setBudgetPlaces(int budgetPlaces) {
        this.budgetPlaces = budgetPlaces;
    }

    public int getContractPlaces() {
        return contractPlaces;
    }

    public void setContractPlaces(int contractPlaces) {
        this.contractPlaces = contractPlaces;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "faculty_id=" + facultyid +
                ", title='" + title + '\'' +
                ", totalPlaces=" + totalPlaces +
                ", budgetPlaces=" + budgetPlaces +
                ", contractPlaces=" + contractPlaces +
                '}';
    }
}
