package com.revlearn.team1.service.transaction;

import java.util.List;

import com.revlearn.team1.model.TransactionModel;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;

import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;

@Service
public interface TransactionService {
    TransactionResponseDTO checkout(TransactionRequestDTO transaction) throws StripeException;
}
