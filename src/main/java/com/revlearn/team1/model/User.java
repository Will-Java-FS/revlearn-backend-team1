package com.revlearn.team1.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "\"user\"") // user is a reserved keyword in H2 testing db, needs quotes to properly parse
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
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

}