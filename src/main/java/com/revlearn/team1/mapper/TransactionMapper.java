package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.TransactionDTO;
import com.revlearn.team1.model.TransactionModel;

public class TransactionMapper
{
    public TransactionDTO toDTO(TransactionModel transaction)
    {
        return new TransactionDTO(
                transaction.getTo_user(),
                transaction.getFrom_user(),
                transaction.getPrice(),
                transaction.getDescription()
        );
    }

    public TransactionModel fromDTO(TransactionDTO transactionDTO)
    {
        TransactionModel transaction = new TransactionModel();
        transaction.setTo_user(transactionDTO.toUser());
        transaction.setFrom_user(transactionDTO.fromUser());
        transaction.setPrice(transactionDTO.price());
        transaction.setDescription(transactionDTO.description());

        return transaction;
    }
}
