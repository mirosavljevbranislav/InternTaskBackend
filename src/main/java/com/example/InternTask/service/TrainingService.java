package com.example.InternTask.service;

import com.example.InternTask.exception.TrainerExceptions.TrainerNotFoundException;
import com.example.InternTask.exception.TrainingExceptions.InvalidTrainingTimeException;
import com.example.InternTask.exception.TrainingExceptions.TrainingNotFoundException;
import com.example.InternTask.exception.TrainingExceptions.TrainingOverlapException;
import com.example.InternTask.model.DTO.QueTrainingDTO;
import com.example.InternTask.model.Trainer;
import com.example.InternTask.model.Training;
import com.example.InternTask.repository.MapRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TrainingService {
    private final MapRepo mapRepo;
    private final EmailService emailService;

    public TrainingService(MapRepo mapRepo, EmailService emailService) {
        this.mapRepo = mapRepo;
        this.emailService = emailService;
    }

    public void addTraining(UUID trainerId, Training training){
        Trainer trainer = mapRepo.getTrainers().get(trainerId.toString());
        if (trainer == null) {
            throw new TrainerNotFoundException("Trainer not found...");
        }

        int minute = training.getTrainingTime().getMinute();
        if (minute != 0 && minute != 30) {
            throw new InvalidTrainingTimeException("Training must start at full hour, or half hour (8:00, 8:30...)");
        }

        LocalDateTime newStart = training.getTrainingTime();
        LocalDateTime newEnd = newStart.plusMinutes(training.getTrainingDuration());

        for (Training existing : trainer.getTrainings()) {
            LocalDateTime existingStart = existing.getTrainingTime();
            LocalDateTime existingEnd = existingStart.plusMinutes(existing.getTrainingDuration());

            boolean overlaps = !(newEnd.isBefore(existingStart) || newStart.isAfter(existingEnd.minusSeconds(1)));

            if (overlaps) {
                throw new TrainingOverlapException("That term is already taken, please try other ones.");
            }
        }
        mapRepo.getTrainers().get(trainerId.toString()).getTrainings().add(training);
    }

    public void queueTrainingService(String trainerId, Training queuedTraining) {
        if (!mapRepo.getTrainers().containsKey(trainerId)){
            throw new TrainerNotFoundException("Trainer with that ID not found");
        }
        if (trainerId == null|| trainerId.isBlank()){
            throw new TrainerNotFoundException("Please enter trainer's id");
        }

        Map<String, List<Training>> trainingQueue = mapRepo.getTrainingQueue();

        // Get the list or create a new one if not present
        List<Training> trainingsForTrainer = trainingQueue.computeIfAbsent(trainerId, k -> new ArrayList<>());

        // Add the new training to the list
        trainingsForTrainer.add(queuedTraining);
    }

    public void removeTrainingAsTrainer(String trainerId, UUID trainingId) {
        Trainer trainer = mapRepo.getTrainers().get(trainerId);
        if (trainer == null) {
            throw new TrainerNotFoundException("Trainer not found...");
        }
        List<Training> trainingList = trainer.getTrainings();
        // Find the training first
        Training removedTraining = null;
        Iterator<Training> iterator = trainingList.iterator();
        while (iterator.hasNext()) {
            Training t = iterator.next();
            if (t.getId().equals(trainingId)) {
                removedTraining = t;
                iterator.remove();
                break;
            }
        }

        String emailMessage = "Greetings, unfortunately we have to inform you that your training has been canceled\n" +
                "Training id: " + removedTraining.getId() + "\nTraining type: " + removedTraining.getTrainingType()
                + "\nTraining date and time: " + removedTraining.getTrainingTime();
        try {
            emailService.sendEmail("mirosavljev01@gmail.com", "Training term cancellation", emailMessage);
        } catch (Exception e) {
            // Log the error to console or logger
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace(); // Optional, more detail
        }

        boolean removed = trainingList.removeIf(t -> t.getId().equals(trainingId));
        if (!removed) {
            throw new TrainingNotFoundException("Training with the given ID was not found.");
        }
    }



}
