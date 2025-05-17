package com.example.InternTask.repository;

import com.example.InternTask.model.Trainer;
import com.example.InternTask.model.Training;
import com.example.InternTask.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MapRepo {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Trainer> trainers = new HashMap<>();
    private final Map<String, Training> trainings = new HashMap<>();
    private final Map<String, List<Training>> trainingQueue = new HashMap<>();

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<String, Trainer> getTrainers() {
        return trainers;
    }

    public Map<String, Training> getTrainings() {
        return trainings;
    }

    public Map<String, List<Training>> getTrainingQueue() {
        return trainingQueue;
    }
}
