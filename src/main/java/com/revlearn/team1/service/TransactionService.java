package com.revlearn.team1.service;

import com.revlearn.team1.dto.TransactionRequestDTO;
import com.revlearn.team1.dto.TransactionResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService
{
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transaction);
    public TransactionResponseDTO getTransactionById(int transactionId);
}
