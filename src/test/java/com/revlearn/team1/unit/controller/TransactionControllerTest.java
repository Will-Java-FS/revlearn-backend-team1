package com.revlearn.team1.unit.controller;

import java.util.Map;

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
import com.revlearn.team1.service.transaction.StripeService;
import com.stripe.exception.StripeException;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private TransactionController transactionController;

    private long price;
    private String description;
    private String name;

    @BeforeEach
    void setUp() {
        // Initialize common test data
        price = 10000L;
        description = "Sample Transaction";
        name = "Sample Course";
    }

    @Test
    void checkoutTest() throws StripeException
    {
        // Create expected response DTO
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO("url", "message");

        // Create request DTO
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(1L, name, description, price, 1L);

        // Mock the service method
        when(stripeService.checkout(any(TransactionRequestDTO.class))).thenReturn(transactionResponseDTO);

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
        verify(stripeService, times(1)).checkout(transactionRequestDTO);
    }
}
