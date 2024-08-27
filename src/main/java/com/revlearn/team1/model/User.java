package com.revlearn.team1.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "educators")
    private Set<Course> taughtCourses = new HashSet<>();
    @ManyToMany(mappedBy = "students")
    private Set<Course> enrolledCourses = new HashSet<>();
    @OneToMany(mappedBy = "institution")
    private Set<Course> institutionCourses = new HashSet<>();

}