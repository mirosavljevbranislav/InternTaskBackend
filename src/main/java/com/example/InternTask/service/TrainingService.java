package com.example.InternTask.service;

import com.example.InternTask.exception.TrainerExceptions.TrainerNotFoundException;
import com.example.InternTask.exception.TrainingExceptions.InvalidTrainingTimeException;
import com.example.InternTask.exception.TrainingExceptions.TrainingNotFoundException;
import com.example.InternTask.exception.TrainingExceptions.TrainingOverlapException;
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
    private final TrainerService trainerService;

    public TrainingService(MapRepo mapRepo,
                           EmailService emailService,
                           TrainerService trainerService) {
        this.mapRepo = mapRepo;
        this.emailService = emailService;
        this.trainerService = trainerService;
    }

    private boolean isOverlapping(LocalDateTime newStart, int newDuration, LocalDateTime existingStart, int existingDuration) {
        LocalDateTime newEnd = newStart.plusMinutes(newDuration);
        LocalDateTime existingEnd = existingStart.plusMinutes(existingDuration);

        return !(newEnd.isBefore(existingStart) || newStart.isAfter(existingEnd.minusSeconds(1)));
    }

    private void sendEmail(Training removedTraining,
                           Trainer trainer,
                           String trainerMessage,
                           String userMessage){

        String userEmail = removedTraining.getTrainee().getEmail();
        String trainerEmail = trainer.getEmail();
        String messageDetails = "Training details:\n" + removedTraining.toString();
        String emailSubject = "Training assignment";

        try {
            emailService.sendEmail(trainerEmail, emailSubject, trainerMessage + "\n" + messageDetails);
            emailService.sendEmail(userEmail, emailSubject, userMessage + "\n" + messageDetails);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void addTraining(UUID trainerId, Training training){
        Trainer trainer = trainerService.findTrainerById(trainerId.toString())
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found."));

        int minute = training.getTrainingTime().getMinute();
        if (minute != 0 && minute != 30) {
            throw new InvalidTrainingTimeException("Training must start at full hour, or half hour (8:00, 8:30...)");
        }

        LocalDateTime newStart = training.getTrainingTime();
        int newDuration = training.getTrainingDuration();

        for (Training existing : trainer.getTrainings()) {
            if (isOverlapping(newStart, newDuration, existing.getTrainingTime(), existing.getTrainingDuration())) {
                throw new TrainingOverlapException("That term is already taken, please try other ones.");
            }
        }
        String trainerMessage = "Greetings "+ trainer.getName() + ", a training session has been approved.";
        String userMessage = "Greetings, your training session has been approved.";
        sendEmail(training, trainer, trainerMessage, userMessage);
        mapRepo.getTrainers().get(trainerId.toString()).getTrainings().add(training);
    }

    public void queueTrainingService(String trainerId, Training queuedTraining) {
        List<Training> trainingsForTrainer = getTrainings(trainerId);

        LocalDateTime newStart = queuedTraining.getTrainingTime();
        LocalDateTime newEnd = newStart.plusMinutes(queuedTraining.getTrainingDuration());

        for (Training existing : trainingsForTrainer) {
            LocalDateTime existingStart = existing.getTrainingTime();
            int existingDuration = existing.getTrainingDuration();

            if (isOverlapping(newStart, queuedTraining.getTrainingDuration(), existingStart, existingDuration)) {
                throw new TrainingOverlapException("The queued training overlaps with an existing one.");
            }
        }
        trainingsForTrainer.add(queuedTraining);
    }

    private List<Training> getTrainings(String trainerId) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new TrainerNotFoundException("Please enter trainer's id");
        }
        if (!mapRepo.getTrainers().containsKey(trainerId)) {
            throw new TrainerNotFoundException("Trainer with that ID not found");
        }

        Map<String, List<Training>> trainingQueue = mapRepo.getTrainingQueue();

        List<Training> trainingsForTrainer = trainingQueue.computeIfAbsent(trainerId, k -> new ArrayList<>());
        return trainingsForTrainer;
    }

    public void removeTrainingAsTrainer(String trainerId, UUID trainingId) {
        Trainer trainer = trainerService.findTrainerById(trainerId)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found."));
        if (trainer == null) {
            throw new TrainerNotFoundException("Trainer not found...");
        }
        List<Training> trainingList = trainer.getTrainings();
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
        String trainerMessage = "Greetings "+ trainer.getName() + ", a training session has been canceled.";
        String userMessage = "Greetings, unfortunately your training session has been canceled.";
        sendEmail(removedTraining, trainer, trainerMessage, userMessage);
        boolean removed = trainingList.removeIf(t -> t.getId().equals(trainingId));
        if (!removed) {
            throw new TrainingNotFoundException("Training with the given ID was not found.");
        }
    }

    public void removeTrainingAsUser(String trainerId, UUID trainingId) {
        boolean trainingRemoved = mapRepo.getTrainingQueue().values().stream()
                .anyMatch(trainings -> trainings.removeIf(t -> t.getId().equals(trainingId)));
        if (!trainingRemoved) {
            throw new TrainingNotFoundException("Training with the given ID was not found in the queue.");
        }
    }



}
