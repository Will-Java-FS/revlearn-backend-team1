package com.revlearn.team1;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.revlearn.team1.model.PaymentModel;
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
import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.transaction.TransactionServiceImp;

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
        PaymentModel fromUser = new PaymentModel();
        PaymentModel toUser = new PaymentModel();
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
        PaymentModel fromUser = new PaymentModel();
        PaymentModel toUser = new PaymentModel();
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
        PaymentModel fromUser = new PaymentModel();
        PaymentModel toUser = new PaymentModel();
        transactions.add(new TransactionResponseDTO(toUser, fromUser, 100.0f, "Sample Transaction"));

        when(transactionService.getTransactions()).thenReturn(transactions);

        ResponseEntity<List<TransactionResponseDTO>> response = transactionController.getTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(transactionService, times(1)).getTransactions();
    }
}
