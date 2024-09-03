package com.revlearn.team1.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // for our own record of transaction ids

    private String name; // course name
    private String description; // course description
    private long price; // price of the course
    private long quantity; // should always be 1 but stripe expects this in request

    @ManyToOne
    @JoinColumn(name = "course_id") // This should match the column name in the Course entity
    @Nullable
    @JsonBackReference("course-transactions")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "to_user_id") // Rename the column to avoid conflict
    @Nullable
    @JsonBackReference("user-to-transaction")
    private User toUser;

    @ManyToOne
    @JoinColumn(name = "from_user_id") // Rename the column to avoid conflict
    @Nullable
    @JsonBackReference("user-from-transaction")
    private User fromUser;
}

