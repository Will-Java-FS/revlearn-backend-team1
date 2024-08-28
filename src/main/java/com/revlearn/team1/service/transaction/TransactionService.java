package com.revlearn.team1.service.transaction;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;

@Service
public interface TransactionService {
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transaction);

    public TransactionResponseDTO getTransactionById(int transactionId);

    List<TransactionResponseDTO> getTransactions();

    void deleteTransactionById(int transactionId);
}
