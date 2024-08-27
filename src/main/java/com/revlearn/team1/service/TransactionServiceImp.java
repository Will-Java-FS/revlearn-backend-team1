package com.revlearn.team1.service;

import com.revlearn.team1.dto.TransactionDTO;
import com.revlearn.team1.exceptions.CourseNotFoundException;
import com.revlearn.team1.exceptions.TransactionNotFoundException;
import com.revlearn.team1.mapper.TransactionMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.TransactionModel;
import com.revlearn.team1.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImp implements TransactionService
{
    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private TransactionMapper transactionMapper;


    @Override
    public TransactionDTO createTransaction(TransactionDTO transaction)
    {
        TransactionModel transactionModel = transactionMapper.fromDTO(transaction);
        TransactionModel savedTransaction = transactionRepo.save(transactionModel);
        return transactionMapper.toDTO(savedTransaction);
    }

    @Override
    public TransactionDTO getTransactionById(int transactionId)
    {
        TransactionModel retrievedTransaction = transactionRepo.findById(transactionId).orElseThrow(
                () -> new TransactionNotFoundException(String.format("Could not find transaction by Id in Database.  Course ID: %d", transactionId))
        );
        return transactionMapper.toDTO(retrievedTransaction);
    }
}
