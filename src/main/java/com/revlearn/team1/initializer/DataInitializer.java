package com.revlearn.team1.initializer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements ApplicationRunner {

    @Value("${SPRING_API_URL}")
    private String apiUrl;

    @Value("${app.runner.enabled}")
    private boolean isRunnerEnabled;

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (!isRunnerEnabled) {
            logger.info("Skipping data initialization...");
            return;
        }
        logger.info("Initializing data...");
        loadInitialData();
    }

    private void loadInitialData() throws IOException {
        JsonNode rootNode = objectMapper.readTree(new ClassPathResource("initial-data.json").getInputStream());

        createInitialUsers(rootNode.path("users"));
        createInitialCourses(rootNode.path("courses"));
        createInitialTransactions(rootNode.path("transactions"));
        createInitialModules(rootNode.path("modules"));
    }


    private void createInitialUsers(JsonNode usersNode) {
        String requestUrl = apiUrl + "/user/register";
        for (JsonNode userNode : usersNode) {
            sendRequest(requestUrl, HttpMethod.POST, userNode);
        }
        logger.info("Initial users created.");
    }

    private void createInitialCourses(JsonNode coursesNode) {
        String requestUrl = apiUrl + "/course";
        for (JsonNode courseNode : coursesNode) {
            sendRequest(requestUrl, HttpMethod.POST, courseNode);
        }
    }

    private void createInitialTransactions(JsonNode transactionsNode) {
        String requestUrl = apiUrl + "/transaction";
        for (JsonNode transactionNode : transactionsNode) {
            sendRequest(requestUrl, HttpMethod.POST, transactionNode);
        }
    }

    private void createInitialModules(JsonNode modulesNode) {
        String requestUrl = apiUrl + "/module";
        for (JsonNode moduleNode : modulesNode) {
            sendRequest(requestUrl, HttpMethod.POST, moduleNode);
        }
    }

    private void sendRequest(String url, HttpMethod method, JsonNode requestBody) {
        try {
            HttpEntity<JsonNode> requestEntity = new HttpEntity<>(requestBody);
            logger.info("Sending request to URL: " + url);
            logger.info("Request body: " + requestBody.toPrettyString());

            ResponseEntity<JsonNode> response = restTemplate.exchange(url, method, requestEntity, JsonNode.class);

            if (response.getStatusCode().is2xxSuccessful())  {
                logger.info("Request successful: " + response.getBody());
            } else {
                logger.error(
                        "Error in request: " + response.getStatusCode() + ", Response body: " + response.getBody());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while sending request: ", e);
        }
    }

}