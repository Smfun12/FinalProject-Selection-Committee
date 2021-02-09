package com.example.FinalProject.services;

import com.example.FinalProject.entities.Roles;
import com.example.FinalProject.entities.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class StudentDetailsImpl implements UserDetails {

    private Student student;

    public StudentDetailsImpl(Student student){
        this.student = student;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Roles> roles = student.getRolesSet();
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        for (Roles roles1 : roles){
            authorityList.add(new SimpleGrantedAuthority(Roles.USER.name()));
        }
        return authorityList;
    }

    @Override
    public String getPassword() {
        return student.getPassword();
    }

    @Override
    public String getUsername() {
        return student.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
