package com.revlearn.team1.unit.controller;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.revlearn.team1.controller.TransactionController;
import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.transaction.TransactionService;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private User toUser;
    private User fromUser;
    private float price;
    private String description;

    @BeforeEach
    void setUp() {
        // Initialize common test data
        toUser = new User("toUser123");
        fromUser = new User("fromUser123");
        price = 100.0f;
        description = "Sample Transaction";
    }

    @Test
    void createTransactionTest() {
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO(toUser, fromUser, price,
                description);
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(1L, 2L, price, description);

        when(transactionService.createTransaction(any(TransactionRequestDTO.class))).thenReturn(transactionResponseDTO);

        ResponseEntity<TransactionResponseDTO> response = transactionController
                .createTransaction(transactionRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transactionResponseDTO, response.getBody());
        verify(transactionService, times(1)).createTransaction(transactionRequestDTO);
    }

    @Test
    void findTransactionByIdTest() {
        int id = 1;
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO(toUser, fromUser, price,
                description);

        when(transactionService.getTransactionById(id)).thenReturn(transactionResponseDTO);

        ResponseEntity<TransactionResponseDTO> response = transactionController.findTransactionById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionResponseDTO, response.getBody());
        verify(transactionService, times(1)).getTransactionById(id);
    }

    @Test
    void getTransactionsTest() {
        List<TransactionResponseDTO> transactions = new ArrayList<>();
        transactions.add(new TransactionResponseDTO(toUser, fromUser, price, description));

        when(transactionService.getTransactions()).thenReturn(transactions);

        ResponseEntity<List<TransactionResponseDTO>> response = transactionController.getTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(transactionService, times(1)).getTransactions();
    }
}
