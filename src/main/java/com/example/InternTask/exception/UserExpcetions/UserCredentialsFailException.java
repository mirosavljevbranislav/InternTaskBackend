package com.example.InternTask.exception.UserExpcetions;

public class UserCredentialsFailException extends RuntimeException {
    public UserCredentialsFailException(String message) {
        super(message);
    }
}
