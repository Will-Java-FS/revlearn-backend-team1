package com.revlearn.team1.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revlearn.team1.dto.TransactionDTO;
import com.revlearn.team1.model.TransactionModel;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.UserService; 

import jakarta.persistence.EntityNotFoundException;

@Component
public class TransactionMapper {

    @Autowired
    private UserService userService; 

    public TransactionDTO toDTO(TransactionModel transaction) {
        return new TransactionDTO(
                transaction.getToUser() != null ? transaction.getToUser().getId() : null,
                        transaction.getFromUser() != null ? transaction.getFromUser().getId() : null,
                transaction.getPrice(),
                transaction.getDescription());
    }

    public TransactionModel fromDTO(TransactionDTO transactionDTO) {
        TransactionModel transaction = new TransactionModel();

        // Fetch the actual User entities from the database
        User toUser = userService.findById(transactionDTO.toUserId())
                .orElseThrow(
                        () -> new EntityNotFoundException("User not found with ID: " + transactionDTO.toUserId()));
        User fromUser = userService.findById(transactionDTO.fromUserId())
                .orElseThrow(
                        () -> new EntityNotFoundException("User not found with ID: " + transactionDTO.fromUserId()));

        transaction.setToUser(toUser);
        transaction.setFromUser(fromUser);
        transaction.setPrice(transactionDTO.price());
        transaction.setDescription(transactionDTO.description());

        return transaction;
    }
}
