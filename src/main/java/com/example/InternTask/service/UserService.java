package com.example.InternTask.service;

import com.example.InternTask.exception.AuthExceptions.InvalidEmailException;
import com.example.InternTask.exception.AuthExceptions.InvalidPhoneNumberException;
import com.example.InternTask.exception.UserExpcetions.UserAlreadyExistsException;
import com.example.InternTask.model.User;
import com.example.InternTask.repository.MapRepo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final MapRepo mapRepo;

    public UserService(MapRepo mapRepo) {
        this.mapRepo = mapRepo;
    }

    public void addUser(User user) {
        String phone = user.getPhone();
        if (phone == null || phone.length() != 10) {
            throw new InvalidPhoneNumberException("Phone number must be 10 digits long.");
        }
        if (user.getEmail() == null || !user.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new InvalidEmailException("Invalid email format.");
        }
        boolean emailExists = mapRepo.getUsers().values().stream()
                .anyMatch(existingUser -> user.getEmail().equalsIgnoreCase(existingUser.getEmail()));
        if (emailExists) {
            throw new UserAlreadyExistsException("User with that email already exists.");
        }
        if (mapRepo.getUsers().containsKey(phone)) {
            throw new UserAlreadyExistsException("User with that phone already exists"); // User already exists
        }
        mapRepo.getUsers().put(phone, user);
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
