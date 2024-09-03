package com.revlearn.team1.unit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.TransactionModel;
import com.stripe.exception.StripeException;
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
    private long price;
    private String description;

    @BeforeEach
    void setUp() {
        // Initialize common test data
        toUser = new User("toUser123");
        fromUser = new User("fromUser123");
        price = 10000L;
        description = "Sample Transaction";
    }

    @Test
    void checkoutTest() throws StripeException
    {
        // Create expected response DTO
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO("url", "message");

        // Create request DTO
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(1,
                new TransactionModel(1L, 2L, 2, "Course", price, 1L, new Course(), fromUser, toUser));

        // Mock the service method
        when(transactionService.checkout(any(TransactionRequestDTO.class))).thenReturn(transactionResponseDTO);

        // Call the controller method
        ResponseEntity<Map<String, String>> response = transactionController.checkout(transactionRequestDTO);

        // Verify response status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify the response body matches the expected values
        Map<String, String> expectedResponse = Map.of(
                "url", transactionResponseDTO.url(),
                "message", transactionResponseDTO.message()
        );
        assertEquals(expectedResponse, response.getBody());

        // Verify service method call
        verify(transactionService, times(1)).checkout(transactionRequestDTO);
    }
}
