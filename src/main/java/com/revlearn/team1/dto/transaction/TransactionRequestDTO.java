package com.revlearn.team1.dto.transaction;

import com.revlearn.team1.model.User;

//TODO replace User type with Long for userId (decouple the request from the User model implementation)
public record TransactionRequestDTO(
                User toUser,
                User fromUser,
                float price,
                String description) {
}
