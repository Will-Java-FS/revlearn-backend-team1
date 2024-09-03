package com.revlearn.team1.service.transaction;

import com.revlearn.team1.dto.transaction.TransactionResponseDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.mapper.TransactionMapper;
import com.revlearn.team1.model.TransactionModel;
import com.revlearn.team1.repository.TransactionRepo;

@Service
public class TransactionServiceImp implements TransactionService {
    @Autowired
    TransactionRepo transactionRepo; // For future use with Kafka

    @Autowired
    TransactionMapper transactionMapper;

    @Value("${STRIPE_API_KEY}")
    private String stripeApiKey;

    @Value("${CLIENT_URL}")
    private String clientUrl;

    @Override
    public TransactionResponseDTO checkout(TransactionRequestDTO request) throws StripeException {
        TransactionModel item = transactionMapper.fromDTO(request);

        // Set the Stripe API key
        Stripe.apiKey = stripeApiKey;

        // Create a Product in Stripe (optional, can be reused or stored in DB)
        var productParams = ProductCreateParams.builder()
                .setName(item.getName())
                .build();
        var product = Product.create(productParams);

        // Create a Price for the product in Stripe
        var priceParams = PriceCreateParams.builder()
                .setProduct(product.getId())
                .setUnitAmount(item.getPrice()) // Price in cents (e.g., $10.00 = 1000 cents)
                .setCurrency("usd")
                .build();
        var price = Price.create(priceParams);

        // Create a checkout session
        var sessionParams = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT) // Payment mode
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(price.getId()) // Use the price created above
                                .setQuantity(item.getQuantity()) // Quantity of items
                                .build()
                )
                .setSuccessUrl(clientUrl + "/checkout-success") // Redirect on successful payment
                .setCancelUrl(clientUrl + "/checkout-cancel") // Redirect if payment is canceled
                .build();

        // TODO: Kafka implementation here to persist the transaction data to our database

        // Create and return the session URL
        Session session = Session.create(sessionParams);
        return new TransactionResponseDTO(session.getUrl(), "Payment Processed!");
    }

}