package com.example.InternTask.controller;

import com.example.InternTask.model.Trainer;
import com.example.InternTask.model.Training;
import com.example.InternTask.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        return ResponseEntity.ok(trainerService.getAllTrainers());
    }

    @GetMapping("/getSchedule")
    public ResponseEntity<List<Training>> getTrainerSchedule(@RequestParam UUID trainerId) {
        List<Training> schedule = trainerService.getTrainerSchedule(trainerId.toString());
        if (schedule == null || schedule.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(schedule);
    }

}
