package com.revlearn.team1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;
import com.revlearn.team1.service.transaction.TransactionServiceImp;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController
{
    @Autowired
    TransactionServiceImp transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionRequestDTO transaction)
    {
        return new ResponseEntity<>(transactionService.createTransaction(transaction), HttpStatus.CREATED);
    }

    @GetMapping("/{id}") // This should be formatted so that only institutions can access this endpoint
    public ResponseEntity<TransactionResponseDTO> findTransactionById(@PathVariable int id)
    {
        return new ResponseEntity<>(transactionService.getTransactionById(id), HttpStatus.OK);
    }

    @GetMapping // This should be formatted so that only institutions can access this endpoint
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions()
    {
        return new ResponseEntity<>(transactionService.getTransactions(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}") // Again, this should only be accessed by an institution
    public HttpStatus deleteTransactionById(@PathVariable int id)
    {
        transactionService.deleteTransactionById(id);
        return HttpStatus.OK; // Delete before error?
    }
}
