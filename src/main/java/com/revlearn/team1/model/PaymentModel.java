package com.revlearn.team1.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PaymentModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "payment_user_id", referencedColumnName = "id")
    User paymentUser;

    private String firstName;
    private String lastName;
    private String email;
    private long cardNumber;
    private String expirationDate;
    private int cvc;
    private int zip;
}
