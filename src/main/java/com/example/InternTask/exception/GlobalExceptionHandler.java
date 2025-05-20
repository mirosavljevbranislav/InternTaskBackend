package com.example.InternTask.exception;

import com.example.InternTask.exception.AuthExceptions.InvalidEmailException;
import com.example.InternTask.exception.AuthExceptions.InvalidPhoneNumberException;
import com.example.InternTask.exception.TrainerExceptions.InvalidTrainerCredentialsException;
import com.example.InternTask.exception.TrainerExceptions.ScheduleNotFoundException;
import com.example.InternTask.exception.TrainerExceptions.TrainerNotFoundException;
import com.example.InternTask.exception.TrainingExceptions.InvalidTrainingTimeException;
import com.example.InternTask.exception.TrainingExceptions.TrainingOverlapException;
import com.example.InternTask.exception.UserExpcetions.UserAlreadyExistsException;
import com.example.InternTask.exception.UserExpcetions.UserCredentialsFailException;
import com.example.InternTask.exception.UserExpcetions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TrainerNotFoundException.class)
    public ResponseEntity<String> handleTrainerNotFound(TrainerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTrainerCredentialsException.class)
    public ResponseEntity<String> handleTrainerNotFound(InvalidTrainerCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidPhoneNumberException.class)
    public ResponseEntity<String> invalidPhoneNumberException(InvalidPhoneNumberException ex) {
        return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<String> invalidEmailException(InvalidEmailException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(UserCredentialsFailException.class)
    public ResponseEntity<String> handleTrainerNotFound(UserCredentialsFailException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleTrainerNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> userAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTrainingTimeException.class)
    public ResponseEntity<String> handleInvalidTime(InvalidTrainingTimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(TrainingOverlapException.class)
    public ResponseEntity<String> handleOverlap(TrainingOverlapException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<String> handleScheduleNotFound(ScheduleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
    }
}
