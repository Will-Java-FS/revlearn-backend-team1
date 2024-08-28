package com.revlearn.team1.initializer;

import java.time.LocalDate;
import java.util.Set;

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

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.dto.TransactionDTO;
import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.UserRepository;
import com.revlearn.team1.service.CourseService;
import com.revlearn.team1.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final UserRepository userRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Value("${SPRING_API_URL}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Initializing data...");
        createDefaultUsers();
        createDummyCourses();
        createDummyTransactions();
    }

    private void createDefaultUsers() {
        User user1 = new User();
        User user2 = new User();

        userRepository.save(user1);
        userRepository.save(user2);

        logger.info("Default users created.");
    }

    private void createDummyCourses() {
        // Fetch existing users to use as educators and students
        User educator = userService.findById(1L).orElseThrow();
        User student = userService.findById(2L).orElseThrow();

        CourseDTO[] courses = {
                new CourseDTO(
                        null, // id is optional and will be auto-generated
                        "Java Programming",
                        "An advanced course on Java programming.",
                        educator.getId(), // institutionId
                        LocalDate.of(2024, 9, 1),
                        LocalDate.of(2024, 12, 15),
                        AttendanceMethod.ONLINE,
                        Set.of(educator.getId()),
                        Set.of(student.getId())),
                new CourseDTO(
                        null, // id is optional and will be auto-generated
                        "Data Structures",
                        "A comprehensive course on data structures.",
                        educator.getId(), // institutionId
                        LocalDate.of(2024, 10, 1),
                        LocalDate.of(2024, 11, 30),
                        AttendanceMethod.IN_PERSON,
                        Set.of(educator.getId()),
                        Set.of(student.getId()))
        };

        for (CourseDTO courseDTO : courses) {
            try {
                CourseDTO createdCourse = courseService.createCourse(courseDTO);
                logger.info("Course created: " + createdCourse);
            } catch (Exception e) {
                logger.error("Error creating course: ", e);
            }
        }
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