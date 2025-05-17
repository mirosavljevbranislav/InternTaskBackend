package com.example.InternTask.service;

import com.example.InternTask.exception.TrainerExceptions.ScheduleNotFoundException;
import com.example.InternTask.exception.TrainerExceptions.TrainerNotFoundException;
import com.example.InternTask.exception.TrainingExceptions.InvalidTrainingTimeException;
import com.example.InternTask.exception.TrainingExceptions.TrainingNotFoundException;
import com.example.InternTask.exception.TrainingExceptions.TrainingOverlapException;
import com.example.InternTask.model.Trainer;
import com.example.InternTask.model.Training;
import com.example.InternTask.model.User;
import com.example.InternTask.repository.MapRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    private final MapRepo mapRepo;

    public UserService(MapRepo mapRepo) {
        this.mapRepo = mapRepo;
    }

    public boolean addUser(User user) {
        if (mapRepo.getUsers().containsKey(user.getPhone())) {
            return false; // user already exists
        }
        mapRepo.getUsers().put(user.getPhone(), user);
        return true;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(mapRepo.getUsers().values());
    }

    public User getUserById(String id) {
        return mapRepo.getUsers().get(id);
    }

    public User getUserByPhoneNumber(String phoneNumber){
        return mapRepo.getUsers().get(phoneNumber);
    }





}


//package com.example.InternTask.service;
//
//import com.example.InternTask.exception.UserExpcetions.UserNotFoundException;
//import com.example.InternTask.model.User;
//import com.example.InternTask.repository.UserRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Random;
//
//@Service
//public class UserService {
//    private final UserRepo userRepo;
//
//    @Autowired
//    public UserService(UserRepo userRepo) {
//        this.userRepo = userRepo;
//    }
//
//    public User addUser(User user) {
//        return userRepo.save(user);
//    }
//
//    public List<User> findAllUsers() {
//        return userRepo.findAll();
//    }
//
//    public User updateUser(User user) {
//        return userRepo.save(user);
//    }
//
//    public User findUserById(Long id) {
//        return userRepo.findUserById(id)
//                .orElseThrow(() -> new UserNotFoundException("User by that id was not found..."));
//    }
//
//    public void deleteUser(Long id) {
//         userRepo.deleteUserById(id);
//    }
//}
