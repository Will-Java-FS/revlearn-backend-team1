package com.revlearn.team1.model;

import com.revlearn.team1.enums.AttendanceMethod;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Course {

    @ManyToMany
    @JoinTable(
            name = "course_educators",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "educator_id")
    )
    private Set<User> educators = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<User> students = new HashSet<>();
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<DiscussionPost> discussionPosts = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne
    private CourseContent courseContent;
    @ManyToOne
    @JoinColumn(name = "institution_id"
            //TODO: uncomment this once User model is implemented
            // nullable = false
    )
    private User institution;
    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceMethod attendanceMethod;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
