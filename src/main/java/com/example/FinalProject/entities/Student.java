package com.example.FinalProject.entities;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "students")
public class Student {
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
    @Length(max = 10)
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

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "student_roles",
//            joinColumns = @JoinColumn(name = "student_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Roles> rolesSet = new HashSet<>();

    @Column(name = "enabled")
    private boolean enabled = true;

    private boolean accountNonLocked;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Student() {

    }
    public Student(String email) {
        this.email = email;
    }
    @Builder
    public Student(String email,String password,String login,
                   String city, String district, String school,
                   Set<Roles> rolesSet) {
        this.email = email;
        this.password = password;
        this.login = login;
        this.city = city;
        this.district = district;
        this.school = school;
        this.rolesSet = rolesSet;
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
                ", rolesSet=" + rolesSet +
                '}';
    }

}
