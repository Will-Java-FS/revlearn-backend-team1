package com.revlearn.team1.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "\"user\"") // user is a reserved keyword in H2 testing db, needs quotes to properly parse
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "institution")
    private List<Program> programs;
}