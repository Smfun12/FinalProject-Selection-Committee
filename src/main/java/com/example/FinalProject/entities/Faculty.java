package com.example.FinalProject.entities;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Set;

@Entity
@Table(name = "faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "faculty_id")
    private long faculty_id;

    @NotBlank
    @Length(min = 3)
    @Column(name = "title")
    private String title;

    @Positive
    @Column(name = "totalPlaces")
    private int totalPlaces;

    @Positive
    @Column(name = "budgetPlaces")
    private int budgetPlaces;

    @Positive
    @Column(name = "contractPlaces")
    private int contractPlaces;

//    @OneToMany
//    @CollectionTable(name = "students", joinColumns = @JoinColumn(name = "faculty_id"))
//    private Set<Student> studentSet;

    private Faculty(){

    }

    @Builder
    public Faculty(@NotBlank @Length(min = 10) String title, @Positive int totalPlaces,
                   @Positive int budgetPlaces, @Positive int contractPlaces) {
        this.title = title;
        this.totalPlaces = totalPlaces;
        this.budgetPlaces = budgetPlaces;
        this.contractPlaces = contractPlaces;
    }

    public long getFaculty_id() {
        return faculty_id;
    }

    public void setFaculty_id(long faculty_id) {
        this.faculty_id = faculty_id;
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
                "faculty_id=" + faculty_id +
                ", title='" + title + '\'' +
                ", totalPlaces=" + totalPlaces +
                ", budgetPlaces=" + budgetPlaces +
                ", contractPlaces=" + contractPlaces +
                '}';
    }
}
