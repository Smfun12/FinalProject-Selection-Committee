package com.example.FinalProject.entities.dto;

import com.example.FinalProject.entities.models.Faculty;
import com.example.FinalProject.entities.models.Roles;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
public class StudentDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long studentid;
    @NotNull
    @Email(message = "Email cannot be null")
    @Column(name = "email")
    private String email;

    @NotNull
    @Length(min = 5)
    @Column(name = "login")
    private String login;
    @NotNull
    @Column(name = "password")
    private String password;

    @Length(min = 4)
    @Column(name = "city")
    private String city;

    @Length(min = 4)
    @Column(name = "district")
    private String district;

    @Length(min = 4)
    @Column(name = "school")
    private String school;

    public boolean budget = true;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "students_faculties",
            joinColumns = {
                    @JoinColumn(name = "student_id", referencedColumnName = "studentid",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "faculty_id", referencedColumnName = "facultyid",
                            nullable = false, updatable = false)})
    private Set<Faculty> faculties
            = new HashSet<>();

    @ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "student_role", joinColumns = @JoinColumn(name = "student_id"))
    @Enumerated(EnumType.STRING)
    private Set<Roles> rolesSet;

    @Column(name = "enabled")
    private boolean enabled = true;

    private int firstGrade;

    private int secondGrade;

    private int thirdGrade;
}
