package com.example.InternTask.model.DTO;

import com.example.InternTask.model.Training;

import java.util.UUID;

public class QueTrainingDTO {
    private UUID trainerId;
    private Training training;

    public QueTrainingDTO() {
    }

    public QueTrainingDTO(UUID trainerId, Training training) {
        this.trainerId = trainerId;
        this.training = training;
    }


    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }
}
