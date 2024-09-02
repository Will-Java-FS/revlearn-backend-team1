package com.revlearn.team1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.revlearn.team1.enums.AttendanceMethod;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revlearn.team1.enums.AttendanceMethod;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "course_educators",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "educator_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "educator_id"})  // Ensure uniqueness
    )
    @JsonIgnore
    private List<User> educators = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "student_id"})  // Ensure uniqueness
    )
    private List<User> students = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    @JsonIgnore
    private User institution;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<DiscussionPost> discussionPosts = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_content_id", nullable = true, referencedColumnName = "id")
    private CourseContent courseContent;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceMethod attendanceMethod;

    @OneToMany(mappedBy = "course")
    @JsonManagedReference("course-transactions")
    private List<TransactionModel> transactions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    //Most courses will only have one program, but some might be part of more
    //ie Math 75 is for physics degrees and computer science degrees
    @ManyToMany
    private List<Program> programs;
}
