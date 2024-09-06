package com.revlearn.team1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "progress")
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progress_id;

    private boolean completed;

    private Float completedProgress;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    @OneToOne
    @JoinColumn(name = "course_module_id")
    private CourseModule courseModule;

    @OneToOne
    @JoinColumn(name = "module_page_id")
    private ModulePage modulePage;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    //, referencedColumnName = "user", nullable = false)
    private User user;

    @Override
    public String toString() {
        return "Progress{ progress_id:" + progress_id +
                ", completed:" + completed +
                ", completedProgress:" + completedProgress +
                ", course:" + course +
                ", module:" + courseModule +
                ", page_id" + modulePage +
                ", user:" + user;

    }

    public boolean getCompleted() {
        return completed;
    }

}
