package com.revlearn.team1.service;

import com.revlearn.team1.dto.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService
{
    TransactionDTO createTransaction(TransactionDTO transaction);
    TransactionDTO getTransactionById(int transactionId);
    List<TransactionDTO> getTransactions();
    void deleteTransactionById(int transactionId);
}
