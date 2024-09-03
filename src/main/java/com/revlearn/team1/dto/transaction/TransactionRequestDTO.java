package com.revlearn.team1.dto.transaction;

import com.revlearn.team1.model.TransactionModel;

import java.util.List;

public record TransactionRequestDTO(
        long id, // user id
        String name,
        String description,
        long price,
        long quantity
) { }
