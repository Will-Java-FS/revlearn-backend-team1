package com.revlearn.team1.dto.transaction;
public record TransactionRequestDTO(
        Long toUserId,
        Long fromUserId,
        float price,
        String description) {
}
