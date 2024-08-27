package com.revlearn.team1.controller;

import com.revlearn.team1.dto.TransactionDTO;
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
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transaction)
    {
        return new ResponseEntity<>(transactionService.createTransaction(transaction), HttpStatus.CREATED);
    }

    @GetMapping("/{id}") // This should be formatted so that only institutions can access this endpoint
    public ResponseEntity<TransactionDTO> findTransactionById(@PathVariable int id)
    {
        return new ResponseEntity<>(transactionService.getTransactionById(id), HttpStatus.OK);
    }
}
