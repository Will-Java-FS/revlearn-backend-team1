package com.revlearn.team1.service.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;
import com.revlearn.team1.mapper.TransactionMapper;
import com.revlearn.team1.model.TransactionModel;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeService {

    @Autowired
    TransactionMapper transactionMapper; // For future use with Kafka

    @Autowired
    KafkaTemplate<String, Session> kafkaTemplate;

    @Value("${STRIPE_API_KEY}")
    private String stripeApiKey;

    @Value("${CLIENT_URL}")
    private String clientUrl;

    public TransactionResponseDTO checkout(TransactionRequestDTO request)
    {
        try {
            // Set the Stripe API key
            Stripe.apiKey = stripeApiKey;

            // Define product data directly within PriceData
            var productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName(request.name()) // Set the product name or description
                    .setDescription(request.description()) // Set the product description
                    .build();

            // Define PriceData with the product information
            var priceData = SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("usd")
                    .setUnitAmount(request.price()) // Price in cents (e.g., $10.00 = 1000 cents)
                    .setProductData(productData) // Use the defined product data
                    .build();

            // Define LineItem with PriceData
            var lineItem = SessionCreateParams.LineItem.builder()
                    .setPriceData(priceData) // Use PriceData directly
                    .setQuantity(request.quantity()) // Quantity of items
                    .build();

            // Define SessionCreateParams
            var sessionParams = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT) // Payment mode
                    .addLineItem(lineItem) // Add LineItem
                    .setSuccessUrl(clientUrl + "/checkout-success") // Redirect on successful payment
                    .setCancelUrl(clientUrl + "/checkout-cancel") // Redirect if payment is canceled
                    .build();

            // Create and return the checkout session URL
            Session session = Session.create(sessionParams);

            // Message to send to Kafka
            Message<TransactionModel> message = MessageBuilder
                    .withPayload(transactionMapper.fromDTO(request)) // Convert the request DTO to a model
                    .setHeader(KafkaHeaders.TOPIC, "transactions")
                    .build();

            kafkaTemplate.send(message); // Send the message to Kafka to persist the transaction to the database

            // Return the transaction response DTO
            return new TransactionResponseDTO(session.getUrl(), "Payment Processed!");
        } catch (StripeException e) {
            // Handle StripeException and return an error response
            return new TransactionResponseDTO(null, "Payment Failed: " + e.getMessage());
        }
    }
}