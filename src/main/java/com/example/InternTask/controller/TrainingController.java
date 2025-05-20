package com.example.InternTask.controller;

import com.example.InternTask.model.DTO.QueTrainingDTO;
import com.example.InternTask.model.Training;
import com.example.InternTask.repository.MapRepo;
import com.example.InternTask.service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trainings")

public class TrainingController {

    private final MapRepo mapRepo;
    private final TrainingService trainingService;


    public TrainingController(MapRepo mapRepo, TrainingService trainingService) {
        this.mapRepo = mapRepo;
        this.trainingService = trainingService;
    }

    @PostMapping("/trainerAssign")
    public ResponseEntity<Training> assignTraining(
            @RequestHeader("X-Trainer-Id") UUID trainerId,
            @RequestBody Training training) {
        trainingService.addTraining(trainerId, training);
        return ResponseEntity.ok(training);
    }

    @PostMapping("/userAssign")
    public ResponseEntity<Training> assignTrainingAsUser( @RequestBody QueTrainingDTO trainingDTO){
        Training training = trainingService.queueTraining(trainingDTO);
        return ResponseEntity.ok(training);
    }

    @GetMapping("/fetchQueue")
    public ResponseEntity<List<Training>> fetchTrainingQueue(
            @RequestHeader(value = "X-Trainer-Id", required = false, defaultValue = "") UUID trainerId,
            @RequestHeader(value = "X-Trainee-Id", required = false, defaultValue = "") UUID traineeId){

        List<Training> queuedTrainings = trainingService.getQueuedTrainings(
                trainerId != null ? trainerId.toString() : null,
                traineeId != null ? traineeId.toString() : null
        );

        return ResponseEntity.ok(queuedTrainings);
    }

    @DeleteMapping("/removeAsTrainer")
    public ResponseEntity<String> removeTrainingAsTrainer(
            @RequestHeader("X-Trainer-Id") UUID trainerId,
            @RequestParam UUID trainingId){
        trainingService.removeTrainingAsTrainer(trainerId.toString(), trainingId);
        return ResponseEntity.ok("Training removed successfully");
    }

    @DeleteMapping("/removeAsUser")
    public ResponseEntity<String> removeTrainingAsUser(@RequestParam UUID trainingId){
        trainingService.removeTrainingAsUser(trainingId);
        return ResponseEntity.ok("Training removed successfully");
    }

}
