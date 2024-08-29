package com.revlearn.team1.model;

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
    int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "to_id")
    User to_user; //this should be an institution (unless it is a refund)

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "from_id")
    User from_user; //this should be a student (unless it is a refund)

    float price;
    String description;

    //What was purchased
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;}
