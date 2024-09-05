package com.revlearn.team1.service.transaction;

import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;
import com.revlearn.team1.mapper.TransactionMapper;
import com.revlearn.team1.model.TransactionModel;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Autowired
    TransactionMapper transactionMapper;

    @Autowired
    KafkaTemplate<String, Session> kafkaTemplate;

    @Value("${STRIPE_API_KEY}")
    private String stripeApiKey;

    @Value("${CLIENT_URL}")
    private String clientUrl;

    private static final int MAX_FAILED_ATTEMPTS = 100; // number of messages that can be "stored" without a kafka connection before we stop trying
    private int failedAttempts = 0;

    public TransactionResponseDTO checkout(TransactionRequestDTO request) {
        try {
            // Set the Stripe API key
            Stripe.apiKey = stripeApiKey;

            // Define product data directly within PriceData
            var productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName(request.name())
                    .setDescription(request.description())
                    .build();

            // Define PriceData with the product information
            var priceData = SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("usd")
                    .setUnitAmount(request.price())
                    .setProductData(productData)
                    .build();

            // Define LineItem with PriceData
            var lineItem = SessionCreateParams.LineItem.builder()
                    .setPriceData(priceData)
                    .setQuantity(request.quantity())
                    .build();

            // Define SessionCreateParams
            var sessionParams = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .addLineItem(lineItem)
                    .setSuccessUrl(clientUrl + "/checkout-success")
                    .setCancelUrl(clientUrl + "/checkout-cancel")
                    .build();

            // Create and return the checkout session URL
            Session session = Session.create(sessionParams);

            // Check if the number of failed attempts is below the limit
            if (failedAttempts < MAX_FAILED_ATTEMPTS)
            {
                try
                {
                    // Message to send to Kafka
                    Message<TransactionModel> message = MessageBuilder
                            .withPayload(transactionMapper.fromDTO(request))
                            .setHeader(KafkaHeaders.TOPIC, "transactions")
                            .build();

                    kafkaTemplate.send(message); // Send the message to Kafka
                    failedAttempts = 0; // Reset the counter on success
                }
                catch (Exception e)
                {
                    failedAttempts++; // Increment the counter on failure
                }
            }

            // Return the transaction response DTO
            return new TransactionResponseDTO(session.getUrl(), "Payment Processed!");
        }
        catch (StripeException e)
        {
            // Handle StripeException and return an error response
            return new TransactionResponseDTO(null, "Payment Failed: " + e.getMessage());
        }
    }
}