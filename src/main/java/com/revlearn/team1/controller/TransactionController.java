package com.revlearn.team1.controller;

import com.revlearn.team1.dto.TransactionRequestDTO;
import com.revlearn.team1.dto.TransactionResponseDTO;
import com.revlearn.team1.service.TransactionServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
