package com.revlearn.team1.dto.transaction;

public record TransactionRequestDTO(
        long id, // user id
        String name,
        String description,
        long price,
        long quantity
) { }
