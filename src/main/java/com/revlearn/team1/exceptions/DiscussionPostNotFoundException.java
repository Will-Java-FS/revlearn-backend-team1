package com.revlearn.team1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class DiscussionPostNotFoundException extends RuntimeException{

    public DiscussionPostNotFoundException(String message){
        super(message);
    }
}