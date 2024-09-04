package com.revlearn.team1.service.transaction;

import com.revlearn.team1.model.TransactionModel;
import com.revlearn.team1.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImp implements TransactionService
{
    @Autowired
    private TransactionRepo transactionRepo;

    @Override
    @KafkaListener(topics = "transactions", groupId = "transaction-listeners-1")
    public void saveTransaction(TransactionModel transaction)
    {
        // Upon receiving a transaction kafka message, save it to the database
        transactionRepo.save(transaction);
    }
}