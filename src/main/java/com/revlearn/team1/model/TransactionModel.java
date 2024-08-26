package com.revlearn.team1.model;

import jakarta.persistence.*;
import lombok.*;
import com.revlearn.team1.model.User;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TransactionModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "to_id")
    User to_user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "from_id")
    User from_user;

    float price;
    String Description;
}
