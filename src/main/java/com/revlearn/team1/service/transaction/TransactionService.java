package com.revlearn.team1.service.transaction;

import com.revlearn.team1.model.TransactionModel;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService
{
    public void saveTransaction(TransactionModel transaction);
}
