package ru.skypro.lessons.springboot.security_web.security;

import org.springframework.security.core.GrantedAuthority;

public class SimpleGrantedAuthority implements GrantedAuthority {

    private String role;

    public SimpleGrantedAuthority(Authority authority) {
        this.role = authority.getRole();
    }

    @Override
    public String getAuthority() {
        return this.role;
    }
}
