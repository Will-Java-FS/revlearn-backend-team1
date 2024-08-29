package com.revlearn.team1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "\"user\"") // user is a reserved keyword in H2 testing db, needs quotes to properly parse
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true, nullable = false)
    private String username;

    @ManyToMany(mappedBy = "educators")
    @JsonIgnore
    private List<Course> taughtCourses = new ArrayList<>();

    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    private List<Course> enrolledCourses = new ArrayList<>();

    @OneToMany(mappedBy = "institution")
    @JsonIgnore
    private List<Course> institutionCourses = new ArrayList<>();

    public User() {
        this.username = "default-user";
    }

}