package com.revlearn.team1.dto;

public record TransactionDTO(
        Long toUserId,
        Long fromUserId,
        float price,
        String description) {
}
