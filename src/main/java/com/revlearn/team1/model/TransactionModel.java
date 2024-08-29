package com.revlearn.team1.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    private int id;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    @JsonBackReference("user-to-transaction")
    private User toUser;//this should be an institution (unless it is a refund)

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    @JsonBackReference("user-from-transaction")
    private User fromUser;//this should be a student (unless it is a refund)

    private float price;
    private String description;

    //What was purchased
    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference("course-transactions")
    private Course course;

}
