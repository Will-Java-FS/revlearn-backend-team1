package com.revlearn.team1.dto;

import com.revlearn.team1.model.User;

public record TransactionDTO(
        User toUser,
        User fromUser,
        float price,
        String description
)
{}
