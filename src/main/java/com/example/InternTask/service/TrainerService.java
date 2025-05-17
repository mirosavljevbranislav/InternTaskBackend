package com.example.InternTask.service;

import com.example.InternTask.exception.TrainerExceptions.ScheduleNotFoundException;
import com.example.InternTask.model.Trainer;
import com.example.InternTask.model.Training;
import com.example.InternTask.repository.MapRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    private final MapRepo mapRepo;

    public TrainerService(MapRepo mapRepo) {
        this.mapRepo = mapRepo;
    }

    public boolean addTrainer(Trainer trainer) {
        boolean exists = mapRepo.getTrainers().values().stream()
                .anyMatch(t -> t.getTrainerCode() == trainer.getTrainerCode());

        if (exists) {
            return false; // trainer already exists
        }
        mapRepo.getTrainers().put(trainer.getId(), trainer);
        return true;
    }

    public List<Trainer> getAllTrainers() {
        return new ArrayList<>(mapRepo.getTrainers().values());
    }

    public Optional<Trainer> findTrainerById(String trainerId) {
        return mapRepo.getTrainers().values().stream()
                .filter(t -> t.getId().equals(trainerId))  // getId() from User superclass
                .findFirst();
    }

    public Optional<Trainer> findTrainerByCode(int trainerCode) {
        return mapRepo.getTrainers().values().stream()
                .filter(t -> t.getTrainerCode() == trainerCode)
                .findFirst();
    }

    public List<Training> getTrainerSchedule(String trainerId){
        List<Training> listOfTrainings =  mapRepo.getTrainers().get(trainerId).getTrainings();
        if (listOfTrainings.isEmpty() || listOfTrainings == null){
            throw new ScheduleNotFoundException("There are no trainings currently");
        }
        return listOfTrainings;
    }
}
