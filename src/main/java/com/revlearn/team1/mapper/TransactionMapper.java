package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;
import com.revlearn.team1.model.TransactionModel;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {
    private final UserService userService;

    // TODO: @Mapper annotation can automatically generate these methods. Implement
    // later if needed
    public TransactionResponseDTO toDTO(TransactionModel transaction) {
        return new TransactionResponseDTO((long) transaction.getToUser().getId(), (long) transaction.getFromUser().getId(), transaction.getPrice(), transaction.getDescription());
    }

    public TransactionModel fromDTO(TransactionRequestDTO transactionDTO) {
        // Retrieve User entities using the UserService
        User toUser = userService.findById(Math.toIntExact(transactionDTO.toUserId())).orElseThrow(() -> new RuntimeException("User not found with ID: " + transactionDTO.toUserId()));
        User fromUser = userService.findById(Math.toIntExact(transactionDTO.fromUserId())).orElseThrow(() -> new RuntimeException("User not found with ID: " + transactionDTO.fromUserId()));

        TransactionModel transaction = new TransactionModel();
        transaction.setToUser(toUser);
        transaction.setFromUser(fromUser);
        transaction.setPrice(transactionDTO.price());
        transaction.setDescription(transactionDTO.description());

        return transaction;
    }
}
