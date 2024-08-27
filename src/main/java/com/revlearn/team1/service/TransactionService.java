package com.revlearn.team1.service;

import com.revlearn.team1.dto.TransactionDTO;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService
{
    public TransactionDTO createTransaction(TransactionDTO transaction);
    public TransactionDTO getTransactionById(int transactionId);
}
