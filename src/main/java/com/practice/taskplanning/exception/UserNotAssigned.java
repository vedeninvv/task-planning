package com.practice.taskplanning.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotAssigned extends RuntimeException {
    public UserNotAssigned(String message) {
        super(message);
    }
}
