package com.example.InternTask.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
public class Trainer extends User{

    private int trainerCode;
    private List<Training> trainings;

    public Trainer(String name, String phone) {
        super(name, phone);
        Random random = new Random();
        this.trainerCode = random.nextInt(1, 10000);
        this.trainings = new ArrayList<>();
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getPhone() {
        return super.getPhone();
    }

    public int getTrainerCode() {
        return trainerCode;
    }

    public void setTrainerCode(int trainerCode) {
        this.trainerCode = trainerCode;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public void addTraining(Training training){
        this.trainings.add(training);
    }

    public void removeTraining(Training training){
        this.trainings.remove(training);
    }


    @Override
    public String toString() {
        return "Trainer{" +
                "ID: " + getId() +
                "Name: " + getName() +
                "Phone: " + getPhone() +
                "trainerCode: " + trainerCode +
                ", trainings: " + trainings +
                '}';
    }
}
