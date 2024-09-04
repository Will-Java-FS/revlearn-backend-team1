package com.revlearn.team1.exceptions;

import com.revlearn.team1.exceptions.course.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<String> handleCourseNotFoundException(CourseNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceLayerDataAccessException.class)
    public ResponseEntity<Map<String, String>> handleServiceLayerDataAccessException(ServiceLayerDataAccessException ex) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", ex.getMessage());
        if (ex.getCause() != null) {
            responseBody.put("cause", ex.getCause().toString());  // Cause of the error
        }
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<String> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StudentAlreadyEnrolledInCourseException.class)
    public ResponseEntity<String> handleStudentAlreadyEnrolledInCourseException(StudentAlreadyEnrolledInCourseException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentNotEnrolledInCourseException.class)
    public ResponseEntity<String> handleStudentNotEnrolledInCourseException(StudentNotEnrolledInCourseException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EducatorAlreadyTeachesCourseException.class)
    public ResponseEntity<String> handleEducatorAlreadyTeachesCourseException(EducatorAlreadyTeachesCourseException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EducatorDoesNotTeachCourseException.class)
    public ResponseEntity<String> handleEducatorDoesNotTeachCourseException(EducatorDoesNotTeachCourseException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ModuleNotFoundException.class)
    public ResponseEntity<String> handleModuleNotFoundException(ModuleNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
