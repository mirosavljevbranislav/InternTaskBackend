package com.example.InternTask.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public class Training {

    private UUID id;
    private String trainerId;
    private int trainingDuration;
    private String trainingType;

    @JsonFormat(pattern = "dd/MM/yy HH:mm")
    private LocalDateTime trainingTime;
    private User trainee;

    public Training() {
    }

    public Training(int trainingDuration,
                    String trainerId,
                    String trainingType,
                    LocalDateTime trainingTime,
                    User trainee) {
        this.id = UUID.randomUUID();
        this.trainerId = trainerId;
        this.trainingDuration = trainingDuration;
        this.trainingType = trainingType;
        this.trainingTime = trainingTime;
        this.trainee = trainee;
    }


    public UUID getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID();
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public User getTrainee() {
        return trainee;
    }

    public void setTrainee(User trainee) {
        this.trainee = trainee;
    }

    public int getTrainingDuration() {
        return trainingDuration;
    }


    public String getTrainingType() {
        return trainingType;
    }

    public LocalDateTime getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(LocalDateTime trainingTime) {
        this.trainingTime = trainingTime;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    @Override
    public String toString() {
        return "Training Details:\n" +
                "ID: " + id + "\n" +
                "Trainer ID: " + trainerId + "\n" +
                "Duration: " + trainingDuration + " minutes\n" +
                "Type: " + trainingType + "\n" +
                "Date & Time: " + trainingTime + "\n" +
                "Trainee: " + trainee.toString() + "\n";
    }
}
