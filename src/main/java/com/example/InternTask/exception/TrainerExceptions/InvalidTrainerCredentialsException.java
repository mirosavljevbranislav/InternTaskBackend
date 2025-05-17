package com.example.InternTask.exception.TrainerExceptions;

public class InvalidTrainerCredentialsException extends RuntimeException {
    public InvalidTrainerCredentialsException(String message) {
        super(message);
    }
}
