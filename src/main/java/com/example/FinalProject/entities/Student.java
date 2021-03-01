package com.example.FinalProject.entities;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Student class
 */
@Entity
@Table(name = "students")
public class Student implements Comparable<Student>{
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

    public Set<Faculty> getFaculties() {
        return faculties;
    }

    public boolean budget = true;

    public boolean isBudget() {
        return budget;
    }

    public void setBudget(boolean budget) {
        this.budget = budget;
    }

    public void setFaculties(Set<Faculty> faculties) {
        this.faculties = faculties;
    }

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

    public Set<Roles> getRolesSet() {
        return rolesSet;
    }

    public void setRolesSet(Set<Roles> rolesSet) {
        this.rolesSet = rolesSet;
    }

    @ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "student_role", joinColumns = @JoinColumn(name = "student_id"))
    @Enumerated(EnumType.STRING)
    private Set<Roles> rolesSet;

    @Column(name = "enabled")
    private boolean enabled = true;

    private int firstGrade;

    private int secondGrade;

    private int thirdGrade;

    public boolean isEnabled() {
        return enabled;
    }

    public int getFirstGrade() {
        return firstGrade;
    }

    public void setFirstGrade(int firstGrade) {
        this.firstGrade = firstGrade;
    }

    public int getSecondGrade() {
        return secondGrade;
    }

    public void setSecondGrade(int secondGrade) {
        this.secondGrade = secondGrade;
    }

    public int getThirdGrade() {
        return thirdGrade;
    }

    public void setThirdGrade(int thirdGrade) {
        this.thirdGrade = thirdGrade;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int compareTo(Student o) {
        double avgThisGrade = (this.firstGrade + this.secondGrade + this.thirdGrade) / 3.0;
        double avgThatGrade = (o.firstGrade + o.secondGrade + o.thirdGrade) / 3.0;
        return -Double.compare(avgThisGrade,avgThatGrade);
    }

    public Student() {

    }

    public Student(String email) {
        this.email = email;
    }

    @Builder
    public Student(String email, String password, String login,
                   String city, String district, String school,
                   Set<Roles> rolesSet,boolean budget) {
        this.email = email;
        this.password = password;
        this.login = login;
        this.city = city;
        this.district = district;
        this.school = school;
        this.rolesSet = rolesSet;
        this.budget = budget;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String login) {
        this.email = login;
    }

    public long getStudentid() {
        return studentid;
    }

    public void setStudentid(long studentid) {
        this.studentid = studentid;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentid=" + studentid +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", school='" + school + '\'' +
                ", budget=" + budget +
                ", rolesSet=" + rolesSet +
                ", enabled=" + enabled +
                ", firstGrade=" + firstGrade +
                ", secondGrade=" + secondGrade +
                ", thirdGrade=" + thirdGrade +
                '}';
    }

}
