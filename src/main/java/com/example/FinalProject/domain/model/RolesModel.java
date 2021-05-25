package com.example.FinalProject.domain.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Role enum
 */
public enum RolesModel implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
