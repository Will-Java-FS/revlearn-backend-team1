package com.revlearn.team1.service.transaction;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;
import com.revlearn.team1.exceptions.TransactionNotFoundException;
import com.revlearn.team1.mapper.TransactionMapper;
import com.revlearn.team1.model.TransactionModel;
import com.revlearn.team1.repository.TransactionRepo;

@Service
public class TransactionServiceImp implements TransactionService {
    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transaction) {
        TransactionModel transactionModel = transactionMapper.fromDTO(transaction);
        TransactionModel savedTransaction = transactionRepo.save(transactionModel);
        return transactionMapper.toResDTO(savedTransaction);
    }

    @Override
    public TransactionResponseDTO getTransactionById(int transactionId) {
        TransactionModel retrievedTransaction = transactionRepo.findById(transactionId).orElseThrow(
                () -> new TransactionNotFoundException(String
                        .format("Could not find transaction by Id in Database.  Transaction ID: %d", transactionId)));
        return transactionMapper.toResDTO(retrievedTransaction);
    }

    @Override
    public List<TransactionResponseDTO> getTransactions() {
        List<TransactionModel> transactionModels = transactionRepo.findAll();
        return transactionModels.stream()
                .map(transactionMapper::toResDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTransactionById(int transactionId) {
        transactionRepo.findById(transactionId).orElseThrow(
                () -> new TransactionNotFoundException(String.format(
                        "Could not find transaction to delete by that Id in Database.  Transaction ID: %d",
                        transactionId)));

        transactionRepo.deleteById(transactionId); // if no exception, delete the progress_id
    }
}
