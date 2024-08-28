package com.revlearn.team1;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.revlearn.team1.controller.TransactionController;
import com.revlearn.team1.dto.TransactionRequestDTO;
import com.revlearn.team1.dto.TransactionResponseDTO;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.TransactionServiceImp;

class TransactionControllerTest {

    @Mock
    private TransactionServiceImp transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransactionTest() {
        User fromUser = new User(); // Replace with actual initialization
        User toUser = new User(); // Replace with actual initialization
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(toUser, fromUser, 100.0f,
                "Sample Transaction");
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO(toUser, fromUser, 100.0f,
                "Sample Transaction");

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
        User fromUser = new User(); // Replace with actual initialization
        User toUser = new User(); // Replace with actual initialization
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO(toUser, fromUser, 100.0f,
                "Sample Transaction");

        when(transactionService.getTransactionById(id)).thenReturn(transactionResponseDTO);

        ResponseEntity<TransactionResponseDTO> response = transactionController.findTransactionById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionResponseDTO, response.getBody());
        verify(transactionService, times(1)).getTransactionById(id);
    }

    @Test
    void getTransactionsTest() {
        List<TransactionResponseDTO> transactions = new ArrayList<>();
        User fromUser = new User(); // Replace with actual initialization
        User toUser = new User(); // Replace with actual initialization
        transactions.add(new TransactionResponseDTO(toUser, fromUser, 100.0f, "Sample Transaction"));

        when(transactionService.getTransactions()).thenReturn(transactions);

        ResponseEntity<List<TransactionResponseDTO>> response = transactionController.getTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(transactionService, times(1)).getTransactions();
    }
}
