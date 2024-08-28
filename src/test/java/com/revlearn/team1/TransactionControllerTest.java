package com.revlearn.team1;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.revlearn.team1.dto.TransactionDTO;
import com.revlearn.team1.service.TransactionServiceImp;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionServiceImp transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void createTransactionTest() {
        Long toUserId = 1L;
        Long fromUserId = 2L;
        float price = 100.0f;
        String description = "Sample Transaction";
        TransactionDTO transactionDTO = new TransactionDTO(toUserId, fromUserId, price, description);
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenReturn(transactionDTO);

        ResponseEntity<TransactionDTO> response = transactionController.createTransaction(transactionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transactionDTO, response.getBody());
        verify(transactionService, times(1)).createTransaction(transactionDTO);
    }

    @Test
    void findTransactionByIdTest() {
        int id = 1;
        Long toUserId = 1L;
        Long fromUserId = 2L;
        float price = 100.0f;
        String description = "Sample Transaction";
        TransactionDTO transactionDTO = new TransactionDTO(toUserId, fromUserId, price, description);
        when(transactionService.getTransactionById(id)).thenReturn(transactionDTO);

        ResponseEntity<TransactionDTO> response = transactionController.findTransactionById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionDTO, response.getBody());
        verify(transactionService, times(1)).getTransactionById(id);
    }

    @Test
    void getTransactionsTest() {
        Long toUserId = 1L;
        Long fromUserId = 2L;
        float price = 100.0f;
        String description = "Sample Transaction";
        List<TransactionDTO> transactions = new ArrayList<>();
        transactions.add(new TransactionDTO(toUserId, fromUserId, price, description));
        when(transactionService.getTransactions()).thenReturn(transactions);

        ResponseEntity<List<TransactionDTO>> response = transactionController.getTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(transactionService, times(1)).getTransactions();
    }
}
