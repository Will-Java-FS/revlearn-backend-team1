package com.revlearn.team1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "\"user\"") // user is a reserved keyword in H2 testing db, needs quotes to properly parse
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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