package com.revlearn.team1.model;

import jakarta.persistence.*;

@Entity
public class CourseContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Course course;
}
