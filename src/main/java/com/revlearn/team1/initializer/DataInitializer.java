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
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final WebClient webClient = WebClient.create();
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

        //Create an admin (institution) user and store its JWT for future requests
        String jwt = getAdminJWT(loadJson("admin-user.json"));

        createInitialUsers(loadJson("users.json"));
        createInitialCourses(loadJson("courses.json"), jwt);
        createInitialModules(loadJson("modules.json"), jwt);
        createInitialPrograms(loadJson("programs.json"), jwt);
//        TODO: Investigate current implementation of transactions.  It is not clear what is stored in database, now.
//        createInitialTransactions(loadJson("transactions"));

        //Add courses to program(s)
        addCoursesToProgram(getAllCourses(), jwt);
        //Add modules to course(s)


        logger.info("Data initialization complete.");
    }

    private void addCoursesToProgram(JsonNode coursesNode, String jwt) {

        for (JsonNode courseNode : coursesNode) {
            long courseId = courseNode.get("id").asLong();
            //TODO: Verify program with ID 1 exists
            String requestUrl = apiUrl + "/program/1/addCourse/" + courseId;
            logger.info(
                    webClient.patch()
                            .uri(requestUrl)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                            .body(null)
                            .retrieve()
                            .bodyToMono(JsonNode.class)
                            .block()
                            .toString()
            );
        }
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

    private void createInitialModules(JsonNode modulesNode, String jwt) {
        String requestUrl = apiUrl + "/module/course/3";
        for (JsonNode moduleNode : modulesNode) {
            //TODO: Make admin requests after service class is secured
            sendAdminRequest(requestUrl, HttpMethod.POST, moduleNode, jwt);
        }
    }

    private void createInitialPrograms(JsonNode programsNode, String jwt) {
        String requestUrl = apiUrl + "/program";
        for (JsonNode programNode : programsNode) {
            sendAdminRequest(requestUrl, HttpMethod.POST, programNode, jwt);
        }
    }

    private void createInitialPrograms(JsonNode programsNode, String jwt) {
        String requestUrl = apiUrl + "/program";
        for (JsonNode programNode : programsNode) {
            sendAdminRequest(requestUrl, HttpMethod.POST, programNode, jwt);
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

    private JsonNode getAllCourses() {
        String requestUrl = apiUrl + "/course";
        try {
            logger.info("Sending getAllCourses request to URL: " + requestUrl);
            ResponseEntity<JsonNode> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, JsonNode.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Request successful: " + response.getBody().toPrettyString());
                return response.getBody();
            } else {
                logger.error(
                        "Error in request: " + response.getStatusCode() + ", Response body: " + response.getBody());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while sending request: ", e);
        }
        return null;
    }

    private void sendAdminRequest(String url, HttpMethod method, JsonNode requestBody, String jwt) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setBearerAuth(jwt);

            HttpEntity<JsonNode> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
            logger.info("Sending request to URL: " + url);
            if (requestBody != null) logger.info("Request body: " + requestBody.toPrettyString());

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

    private JsonNode loadJson(String fileName) throws IOException {
        try {
            return objectMapper.readTree(new ClassPathResource("initialData/" + fileName).getInputStream());
        } catch (IOException e) {
            logger.error("Error loading JSON file: " + fileName);
            throw e;
        }
    }
}