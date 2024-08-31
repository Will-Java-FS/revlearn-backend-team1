package com.revlearn.team1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Table(name = "\"user\"") // user is a reserved keyword in H2 testing db, needs quotes to properly parse
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private int id;

    @NonNull
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "educators")
    @JsonIgnore
    private List<Course> taughtCourses = new ArrayList<>();

    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    private List<Course> enrolledCourses = new ArrayList<>();

    @OneToMany(mappedBy = "institution")
    @JsonIgnore
    private List<Course> institutionCourses = new ArrayList<>();

    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL)
    @JsonManagedReference("user-to-transaction")
    private List<TransactionModel> proceeds;

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL)
    @JsonManagedReference("user-from-transaction")
    private List<TransactionModel> purchases;


    public User() {
        this.username = "default-user";
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(() -> role);
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}