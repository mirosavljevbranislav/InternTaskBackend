package com.example.InternTask.service;

import com.example.InternTask.exception.AuthExceptions.InvalidEmailException;
import com.example.InternTask.exception.AuthExceptions.InvalidPhoneNumberException;
import com.example.InternTask.exception.TrainerExceptions.ScheduleNotFoundException;
import com.example.InternTask.exception.UserExpcetions.UserAlreadyExistsException;
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

    public void addTrainer(Trainer trainer) {
        if (trainer.getPhone() == null || trainer.getPhone().length() != 10) {
            throw new InvalidPhoneNumberException("Phone number must be 10 digits long.");
        }

        if (trainer.getEmail() == null || !trainer.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new InvalidEmailException("Invalid email format.");
        }

        boolean emailExists = mapRepo.getTrainers().values().stream()
                .anyMatch(existingTrainer -> trainer.getEmail().equalsIgnoreCase(existingTrainer.getEmail()));
        if (emailExists) {
            throw new UserAlreadyExistsException("Trainer already exists with that email.");
        }
        boolean phoneExists = mapRepo.getTrainers().values().stream()
                .anyMatch(existingTrainer -> existingTrainer.getPhone().equals(trainer.getPhone()));

        if (phoneExists) {
            throw new UserAlreadyExistsException("Trainer with that phone number already exists.");
        }
        mapRepo.getTrainers().put(trainer.getId(), trainer);
    }

    public List<Trainer> getAllTrainers() {
        return new ArrayList<>(mapRepo.getTrainers().values());
    }

    public Optional<Trainer> findTrainerById(String trainerId) {
        return mapRepo.getTrainers().values().stream()
                .filter(t -> t.getId().equals(trainerId))
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
