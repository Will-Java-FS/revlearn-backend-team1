package com.revlearn.team1.initializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${SPRING_API_URL}")
    private String apiUrl;
    @Value("${app.runner.enabled}")
    private boolean isRunnerEnabled;

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
//        TODO: Investigate current implementation of transactions. It is not clear what is stored in database.
//        JsonNode transactions = objectMapper.readTree(new ClassPathResource("initialData/transactions.json").getInputStream());
//        createInitialTransactions(rootNode.path("transactions"));

        //Create an admin (institution) user and store its JWT for future requests
        JsonNode adminUser = objectMapper.readTree(new ClassPathResource("initialData/admin-user.json").getInputStream());
        String jwt = getAdminJWT(adminUser);

        JsonNode users = objectMapper.readTree(new ClassPathResource("initialData/users.json").getInputStream());
        JsonNode courses = objectMapper.readTree(new ClassPathResource("initialData/courses.json").getInputStream());
        JsonNode modules = objectMapper.readTree(new ClassPathResource("initialData/modules.json").getInputStream());

        createInitialUsers(users);
        createInitialCourses(courses, jwt);
        createInitialModules(modules);

        logger.info("Data initialization complete.");
    }


    private void createInitialUsers(JsonNode usersNode) {
        String requestUrl = apiUrl + "/user/register";
        for (JsonNode userNode : usersNode) {
            sendRequest(requestUrl, HttpMethod.POST, userNode);
        }
        logger.info("Initial users created.");
    }

    private void createInitialCourses(JsonNode coursesNode, String jwt) {

        try {
            String requestUrl = apiUrl + "/course";
            for (JsonNode courseNode : coursesNode) {
                sendAdminRequest(requestUrl, HttpMethod.POST, courseNode, jwt);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while sending request: ", e);
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

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Request successful: " + response.getBody());
            } else {
                logger.error(
                        "Error in request: " + response.getStatusCode() + ", Response body: " + response.getBody());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while sending request: ", e);
        }
    }

    private void sendAdminRequest(String url, HttpMethod method, JsonNode requestBody, String jwt) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setBearerAuth(jwt);

            HttpEntity<JsonNode> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
            logger.info("Sending request to URL: " + url);
            logger.info("Request body: " + requestBody.toPrettyString());

            ResponseEntity<JsonNode> response = restTemplate.exchange(url, method, requestEntity, JsonNode.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Request successful: " + response.getBody());
            } else {
                logger.error(
                        "Error in request: " + response.getStatusCode() + ", Response body: " + response.getBody());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while sending request: ", e);
        }
    }

    private String getAdminJWT(JsonNode requestBody) {
        String requestUrl = apiUrl + "/user/register";
        HttpEntity<JsonNode> requestEntity = new HttpEntity<>(requestBody);
        logger.info("Sending request to URL: " + requestUrl);
        logger.info("Request body: " + requestBody.toPrettyString());
        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, JsonNode.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Admin JWT Acquisition Successful: " + response.getBody());
            } else {
                logger.error(
                        "Error in request: " + response.getStatusCode() + ", Response body: " + response.getBody());
            }
            return Objects.requireNonNull(response.getBody()).get("JWT").asText();
        } catch (Exception e) {
            logger.error("Exception occurred while sending request: ", e);
            return null;
        }
    }

}