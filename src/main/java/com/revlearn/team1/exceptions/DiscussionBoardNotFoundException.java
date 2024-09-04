package com.revlearn.team1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class DiscussionBoardNotFoundException extends RuntimeException{

    public DiscussionBoardNotFoundException(String message){
        super(message);
    }
}