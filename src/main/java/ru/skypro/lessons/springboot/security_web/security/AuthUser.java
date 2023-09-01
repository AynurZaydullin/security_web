package ru.skypro.lessons.springboot.security_web.security;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Data
@Table(name = "auth_User")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    private boolean isEnabled;
    @JoinTable(joinColumns = @JoinColumn(name = "auth_user_id"), inverseJoinColumns = @JoinColumn(name = "authority_id"))
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Authority> authorityList;
}
