package com.example.FinalProject.domain.service;

import com.example.FinalProject.domain.model.RolesModel;
import com.example.FinalProject.domain.model.StudentModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
public class StudentDetailsImpl implements UserDetails {

    private final StudentModel student;

    public StudentDetailsImpl(StudentModel student) {
        this.student = student;
        log.info(student.toString());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<RolesModel> roles = student.getRolesSet();
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        for (RolesModel roles1 : roles) {
            authorityList.add(new SimpleGrantedAuthority(roles1.name()));
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
