package com.revlearn.team1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "progress")
public class Progress {

    @Id
    @GeneratedValue
    private Long id;

    //Need other entities for relationship
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Student student;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Course course;

    private Float progressPercentage;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public String toString(){
        return "Progress{ id:" + id +
                ", student:" + student +
                ", course:" + course +
                ", progressPercentage:" + progressPercentage +
                ", createdAt:" + createdAt +
                ", updatedAt:" + updatedAt;
    }
}
