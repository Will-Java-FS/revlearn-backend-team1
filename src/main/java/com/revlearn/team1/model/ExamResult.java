package com.revlearn.team1.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade")
    private double grade;

    @CreationTimestamp
    @Column(name = "exam_date", updatable = false, nullable = false)
    private LocalDateTime examDate;


    private long examId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
