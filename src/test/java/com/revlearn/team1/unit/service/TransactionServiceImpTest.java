package com.revlearn.team1.unit.service;

import static org.mockito.Mockito.*;

import com.revlearn.team1.model.TransactionModel;
import com.revlearn.team1.repository.TransactionRepo;
import com.revlearn.team1.service.transaction.TransactionServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TransactionServiceImpTest
{

    @Mock
    private TransactionRepo transactionRepo;

    @InjectMocks
    private TransactionServiceImp transactionServiceImp;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTransactionPersistsTransactionToDatabase()
    {
        TransactionModel transaction = new TransactionModel();
        transactionServiceImp.saveTransaction(transaction);
        verify(transactionRepo, times(1)).save(transaction);
    }

    @Test
    void saveTransactionHandlesNullTransaction()
    {
        transactionServiceImp.saveTransaction(null);
        verify(transactionRepo, never()).save(any(TransactionModel.class));
    }
}