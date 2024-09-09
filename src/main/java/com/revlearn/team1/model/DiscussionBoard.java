package com.revlearn.team1.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="title")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnoreProperties(value = {"discussionPosts"})
    @OneToMany(mappedBy = "discussionBoard", cascade = CascadeType.ALL)
    private List<DiscussionPost> discussionPosts = new ArrayList<>();
}