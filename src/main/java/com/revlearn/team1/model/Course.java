package com.revlearn.team1.model;

import com.revlearn.team1.enums.AttendanceMethod;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    private Object courseContent;

    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    private User institution;

    @ManyToMany
    @JoinTable(
            name = "course_educators",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "educator_id")
    )
    private final Set<User> educators = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private final Set<User> students = new HashSet<>();

    @OneToMany(mappedBy = "course")
    private final Set<DiscussionPost> discussionPosts = new HashSet<>();

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceMethod type;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}