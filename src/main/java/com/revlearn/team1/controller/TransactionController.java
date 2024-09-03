package com.revlearn.team1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stripe.exception.StripeException;
import org.apache.coyote.Response;
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
import com.revlearn.team1.service.transaction.TransactionService;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> checkout(@RequestBody TransactionRequestDTO transactionRequest) throws StripeException
    {
        TransactionResponseDTO transactionResponse = transactionService.checkout(transactionRequest);

        return new ResponseEntity<>(new HashMap<>() {{
            put("url", transactionResponse.url());
            put("message", transactionResponse.message());
        }}, HttpStatus.OK);
    }
}
