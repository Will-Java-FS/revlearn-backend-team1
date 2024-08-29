package com.revlearn.team1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "progress")
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progress_id;

    //Need other entities and database for relationship
    @ManyToOne(cascade = CascadeType.ALL)
    //how to find by progress_id when role == student
    @JoinColumn(name = "user_id")
    private User student;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private Course course;

    private Float progressPercentage;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public String toString(){
        return "Progress{ progress_id:" + progress_id +
                ", student:" + student +
                ", course:" + course +
                ", progressPercentage:" + progressPercentage +
                ", createdAt:" + createdAt +
                ", updatedAt:" + updatedAt;
    }
/*
Don't have user entity yet
    @PrePersist
    @PreUpdate
    public void checkStudent(){}
    if(!"student".equals(student.getRole())){
        throw new IllegalArgumentException("User must be a student");
    }

 */
}
