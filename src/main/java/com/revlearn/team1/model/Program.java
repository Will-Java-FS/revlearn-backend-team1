package com.revlearn.team1.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;  //Short title

    private String description;  //About a paragraph long

    private String department;  //Department that the program is associated with

    private String degree;  //Degree type (e.g. Bachelor's, Master's, etc.)

    private String duration;  //Duration of the program (e.g. 4 years, 2 years, etc.)

    private String location;  //Location of the program (e.g. Online, On-Campus, etc.)

    private String format;  //Format of the program (e.g. Full-time, Part-time, etc.)

    private float cost;  //Cost of the program (e.g. $10,000, $20,000, etc.)

    @ManyToMany
    @JoinTable(
            name = "program_courses",
            joinColumns = @JoinColumn(name = "program_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "program")
    private List<User> students = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
