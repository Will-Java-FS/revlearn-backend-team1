package com.revlearn.team1.initializer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.dto.TransactionDTO;
import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.CourseService;
import com.revlearn.team1.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final UserService userService;
    private final CourseService courseService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Initializing data...");
        loadInitialData();
    }

    private void loadInitialData() throws IOException {
        JsonNode rootNode = objectMapper.readTree(new ClassPathResource("initial-data.json").getInputStream());

        createInitialUsers(rootNode.path("users"));
        createInitialCourses(rootNode.path("courses"));
        createInitialTransactions(rootNode.path("transactions"));
    }

    @Transactional
    private void createInitialUsers(JsonNode usersNode) {
        for (JsonNode userNode : usersNode) {
            String username = userNode.path("username").asText();
            Long userId = userNode.path("id").asLong();
            User user = new User(userId, username);

            userService.save(user);
        }
        logger.info("Initial users created.");
    }

    @Transactional
    private void createInitialCourses(JsonNode coursesNode) {
        for (JsonNode courseNode : coursesNode) {
            CourseDTO courseDTO = new CourseDTO(
                    null, // id is optional and will be auto-generated
                    courseNode.path("name").asText(),
                    courseNode.path("description").asText(),
                    courseNode.path("institutionId").asLong(),
                    LocalDate.parse(courseNode.path("startDate").asText()),
                    LocalDate.parse(courseNode.path("endDate").asText()),
                    AttendanceMethod.valueOf(courseNode.path("type").asText()),
                    toSet(courseNode.path("educatorIds")),
                    toSet(courseNode.path("studentIds")));

            try {
                CourseDTO createdCourse = courseService.createCourse(courseDTO);
                logger.info("Course created: " + createdCourse);
            } catch (Exception e) {
                logger.error("Error creating course: ", e);
            }
        }
    }

    @Transactional
    private void createInitialTransactions(JsonNode transactionsNode) {
        for (JsonNode transactionNode : transactionsNode) {
            TransactionDTO transactionDTO = new TransactionDTO(
                    transactionNode.path("fromUserId").asLong(),
                    transactionNode.path("toUserId").asLong(),
                    (float) transactionNode.path("amount")
                            .asDouble(),
                    transactionNode.path("description").asText());

            // Implement the actual creation using your service or repository
            logger.info("Transaction created: " + transactionDTO);
        }
    }

    private Set<Long> toSet(JsonNode node) {
        Set<Long> set = new HashSet<>();
        if (node.isArray()) {
            for (JsonNode element : node) {
                set.add(element.asLong());
            }
        }
        return set;
    }
}
