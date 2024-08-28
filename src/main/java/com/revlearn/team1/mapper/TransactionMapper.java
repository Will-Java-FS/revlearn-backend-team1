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
                transaction.getTo_payment(),
                transaction.getFrom_payment(),
                transaction.getPrice(),
                transaction.getDescription()
        );
    }

    public TransactionModel fromDTO(TransactionRequestDTO transactionDTO)
    {
        TransactionModel transaction = new TransactionModel();
        transaction.setTo_payment(transactionDTO.toPayment());
        transaction.setFrom_payment(transactionDTO.fromPayment());
        transaction.setPrice(transactionDTO.price());
        transaction.setDescription(transactionDTO.description());

        return transaction;
    }
}
