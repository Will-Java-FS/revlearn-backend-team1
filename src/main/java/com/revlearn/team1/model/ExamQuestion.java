package com.revlearn.team1.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

//TODO: Turn this into interface to handle different question types
@Entity
@Data
public class ExamQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private List<String> options;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;
}
