package com.revlearn.team1.mapper;

import org.springframework.stereotype.Component;

import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;
import com.revlearn.team1.model.TransactionModel;

@Component
public class TransactionMapper
{
    // TODO: @Mapper annotation can automatically generate these methods. Implement
    // later if needed
    public TransactionResponseDTO toDTO(TransactionModel transaction)
    {
        return new TransactionResponseDTO(
                transaction.getTo_user(),
                transaction.getFrom_user(),
                transaction.getPrice(),
                transaction.getDescription()
        );
    }

    public TransactionModel fromDTO(TransactionRequestDTO transactionDTO)
    {
        TransactionModel transaction = new TransactionModel();
        transaction.setTo_user(transactionDTO.toUser());
        transaction.setFrom_user(transactionDTO.fromUser());
        transaction.setPrice(transactionDTO.price());
        transaction.setDescription(transactionDTO.description());

        return transaction;
    }
}
