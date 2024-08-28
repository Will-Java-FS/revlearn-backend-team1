package com.revlearn.team1.dto.transaction;

import com.revlearn.team1.model.PaymentModel;

//TODO replace User type with Long for userId (decouple the request from the User model implementation) -- NEED TO TALK ABOUT THIS MORE
public record TransactionRequestDTO(
                PaymentModel toPayment,
                PaymentModel fromPayment,
                float price,
                String description) {
}
