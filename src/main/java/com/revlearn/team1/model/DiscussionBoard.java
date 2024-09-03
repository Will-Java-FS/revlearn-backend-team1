package com.revlearn.team1.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discussionBoardId;

    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;

    @Column(name="title")
    private String title;
}