package com.example.InternTask.exception.TrainerExceptions;

public class TrainerNotFoundException extends RuntimeException{
    public TrainerNotFoundException(String message) {
        super(message);
    }
}
