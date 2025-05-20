package com.example.InternTask.controller;

import com.example.InternTask.exception.TrainerExceptions.TrainerNotFoundException;
import com.example.InternTask.exception.UserExpcetions.UserNotFoundException;
import com.example.InternTask.model.Trainer;
import com.example.InternTask.model.User;
import com.example.InternTask.service.TrainerService;
import com.example.InternTask.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final TrainerService trainerService;

    public AuthController(UserService userService, TrainerService trainerService) {
        this.userService = userService;
        this.trainerService = trainerService;
    }

    @GetMapping("/login/user")
    public ResponseEntity<User> loginUser(@RequestParam String phoneNumber) {
        User user = userService.getUserByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new UserNotFoundException("User with that phone number not found.");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register/user")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = new User(user.getName(), user.getPhone(), user.getEmail());
        userService.addUser(newUser);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/login/trainer")
    public ResponseEntity<Trainer> loginTrainer(@RequestParam int trainerCode) {
        Trainer trainer = trainerService.findTrainerByCode(trainerCode)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found"));
        return ResponseEntity.ok(trainer);
    }

    @PostMapping("/register/trainer")
    public ResponseEntity<Trainer> registerTrainer(@RequestBody Trainer trainer) {
        trainerService.addTrainer(trainer);
        return ResponseEntity.ok(trainer);
    }
}
