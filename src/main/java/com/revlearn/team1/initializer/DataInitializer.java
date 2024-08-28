package com.revlearn.team1.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.revlearn.team1.dto.TransactionDTO;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final UserRepository userRepository;

    @Value("${SPRING_API_URL}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Initializing data...");
        createDefaultUsers();
        createDummyTransactions();
    }

    private void createDefaultUsers() {
        User user1 = new User();
        User user2 = new User();

        userRepository.save(user1);
        userRepository.save(user2);

        logger.info("Default users created.");
    }

    private void createDummyTransactions() {
        String createTransactionUrl = baseUrl + "/transaction";

        long userId1 = 1L;
        long userId2 = 2L;

        TransactionDTO[] transactions = {
                new TransactionDTO(userId1, userId2, 100.50f, "Payment for services"),
                new TransactionDTO(userId2, userId1, 200.00f, "Refund for overpayment"),
                new TransactionDTO(userId1, userId1, 50.25f, "Personal transfer")
        };

        for (TransactionDTO transaction : transactions) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<TransactionDTO> request = new HttpEntity<>(transaction, headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        createTransactionUrl,
                        HttpMethod.POST,
                        request,
                        String.class);

                logger.info("Transaction created: " + response.getBody());
            } catch (Exception e) {
                logger.error("Error creating transaction: ", e);
            }
        }
    }
}