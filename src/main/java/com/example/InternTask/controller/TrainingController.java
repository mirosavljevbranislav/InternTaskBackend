package com.example.InternTask.controller;

import com.example.InternTask.model.DTO.QueTrainingDTO;
import com.example.InternTask.model.Training;
import com.example.InternTask.repository.MapRepo;
import com.example.InternTask.service.TrainingService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        training.setId();
        trainingService.addTraining(trainerId, training);
        return ResponseEntity.ok(training);
    }

    @PostMapping("/userAssign")
    public ResponseEntity<QueTrainingDTO> assignTrainingAsUser( @RequestBody QueTrainingDTO trainingDTO){
        UUID trainerId = trainingDTO.getTrainerId();
        Training queuedTraining = trainingDTO.getTraining();

        trainingService.queueTrainingService(trainerId.toString(), queuedTraining);
        return ResponseEntity.ok(trainingDTO);
    }

    @GetMapping("/fetchQueue")
    public ResponseEntity<List<QueTrainingDTO>> fetchTrainingQueue(@RequestHeader("X-Trainer-Id") UUID trainerId){

        List<QueTrainingDTO> queuedTrainings = mapRepo.getTrainingQueue()
                .getOrDefault(trainerId.toString(), Collections.emptyList())
                .stream()
                .map(training -> new QueTrainingDTO(trainerId, training))
                .collect(Collectors.toList());

        return ResponseEntity.ok(queuedTrainings);
    }

    @DeleteMapping("/removeAsTrainer")
    public ResponseEntity<String> removeTrainingAsTrainer(
            @RequestHeader("X-Trainer-Id") UUID trainerId,
            @RequestParam UUID trainingId){
        trainingService.removeTrainingAsTrainer(trainerId.toString(), trainingId);
        return ResponseEntity.ok("Training removed successfully");
    }

}
