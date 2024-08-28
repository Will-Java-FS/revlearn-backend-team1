package com.revlearn.team1.model;

import com.revlearn.team1.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;

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

    //Need other entities and database for relationship
    @ManyToOne(cascade = CascadeType.ALL)
    //how to find by id when role == student
    @JoinColumn(name = "student_id")
    private Student student;

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
        return "Progress{ id:" + id +
                ", student:" + student +
                ", course:" + course +
                ", progressPercentage:" + progressPercentage +
                ", createdAt:" + createdAt +
                ", updatedAt:" + updatedAt;
    }
}
