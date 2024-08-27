package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.TransactionRequestDTO;
import com.revlearn.team1.dto.TransactionResponseDTO;
import com.revlearn.team1.model.TransactionModel;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper
{
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
