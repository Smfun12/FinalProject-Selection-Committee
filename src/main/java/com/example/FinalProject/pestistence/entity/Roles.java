package com.example.FinalProject.pestistence.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * Role enum
 */
public enum Roles implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
