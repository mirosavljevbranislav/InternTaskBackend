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
import java.util.stream.Collectors;

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


        trainer.getTrainings().add(training);

        for (Map.Entry<String, List<Training>> entry : mapRepo.getTrainingQueue().entrySet()) {
            List<Training> trainings = entry.getValue();
            boolean removed = trainings.removeIf(trng -> trng.getId().equals(training.getId()));
            if (removed) {
                System.out.println("Removed training with ID: " + training.getId() + " from trainer: " + entry.getKey());
                break;
            }
        }
        sendEmail(training, trainer, trainerMessage, userMessage);
    }

    public Training queueTraining(QueTrainingDTO dto) {
        String trainerId = dto.getTrainerId().toString();
        Training newTraining = new Training(dto.getTraining().getTrainingDuration(),
                dto.getTraining().getTrainerId(),
                dto.getTraining().getTrainingType(),
                dto.getTraining().getTrainingTime(),
                dto.getTraining().getTrainee());
        newTraining.setId();
        Map<String, List<Training>> trainingQueue = mapRepo.getTrainingQueue();
        List<Training> trainerQueue = trainingQueue.computeIfAbsent(trainerId, k -> new ArrayList<>());

        LocalDateTime newStart = newTraining.getTrainingTime();
        int newDuration = newTraining.getTrainingDuration();

        for (Training existing : trainerQueue) {
            if (isOverlapping(newStart, newDuration, existing.getTrainingTime(), existing.getTrainingDuration())) {
                throw new TrainingOverlapException("Training overlaps with an existing queued training.");
            }
        }

        trainerQueue.add(newTraining);
        return newTraining;
    }

    public List<Training> getQueuedTrainings(String trainerId, String traineeId) {
        Map<String, List<Training>> trainingQueue = mapRepo.getTrainingQueue();

        if (trainerId != null && !trainerId.isEmpty()) {
            return trainingQueue.getOrDefault(trainerId, Collections.emptyList());
        }

        if (traineeId != null && !traineeId.isEmpty()) {

            return trainingQueue.values().stream()
                    .flatMap(List::stream)
                    .filter(training -> training.getTrainee() != null && traineeId.equals(training.getTrainee().getId()))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
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
        List<Training> trainingList = mapRepo.getTrainingQueue().get(trainerId);
        Training removedTraining = trainingList.stream()
                .filter(t -> t.getId().equals(trainingId))
                .findFirst()
                .orElse(null);

        if (removedTraining != null) {
            trainingList.remove(removedTraining);
        } else {
            throw new TrainingNotFoundException("Training not found...");
        }

        String trainerMessage = "Greetings "+ trainer.getName() + ", a training session has been canceled.";
        String userMessage = "Greetings, unfortunately your training session has been canceled.";
        sendEmail(removedTraining, trainer, trainerMessage, userMessage);
    }

    public void removeTrainingAsUser(UUID trainingId) {
        boolean trainingRemoved = mapRepo.getTrainingQueue().values().stream()
                .anyMatch(trainings -> trainings.removeIf(t -> t.getId().equals(trainingId)));
        if (!trainingRemoved) {
            throw new TrainingNotFoundException("Training with the given ID was not found in the queue.");
        }
    }



}
