package com.revlearn.team1;

import com.revlearn.team1.dto.TransactionDTO;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.TransactionServiceImp;
import com.revlearn.team1.controller.TransactionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        TransactionDTO transactionDTO = new TransactionDTO(new User(), new User(), 100.0f, "Sample Transaction");
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenReturn(transactionDTO);

        ResponseEntity<TransactionDTO> response = transactionController.createTransaction(transactionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transactionDTO, response.getBody());
        verify(transactionService, times(1)).createTransaction(transactionDTO);
    }

    @Test
    void findTransactionByIdTest() {
        int id = 1;
        TransactionDTO transactionDTO = new TransactionDTO(new User(), new User(), 100.0f, "Sample Transaction");
        when(transactionService.getTransactionById(id)).thenReturn(transactionDTO);

        ResponseEntity<TransactionDTO> response = transactionController.findTransactionById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionDTO, response.getBody());
        verify(transactionService, times(1)).getTransactionById(id);
    }

    @Test
    void getTransactionsTest() {
        List<TransactionDTO> transactions = new ArrayList<>();
        transactions.add(new TransactionDTO(new User(), new User(), 100.0f, "Sample Transaction"));
        when(transactionService.getTransactions()).thenReturn(transactions);

        ResponseEntity<List<TransactionDTO>> response = transactionController.getTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(transactionService, times(1)).getTransactions();
    }
}
