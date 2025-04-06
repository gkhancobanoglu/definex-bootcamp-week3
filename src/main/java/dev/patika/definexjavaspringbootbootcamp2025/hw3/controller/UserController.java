package dev.patika.definexjavaspringbootbootcamp2025.hw3.controller;

import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.User;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.NoSuchUserException;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.list();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User createdUser = userService.create(user);
        return ResponseEntity.created(null).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserProfile(@PathVariable UUID id) {

        try {
            User user = userService.getUserProfile(id);
            return ResponseEntity.ok(user);
        } catch (NoSuchUserException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User user) {


        if (user.getName() == null || user.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.badRequest().body(null);
        }

        if (user.getLicenseNumber() == null || user.getLicenseNumber().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            User updatedUser = userService.update(user);
            return ResponseEntity.ok(updatedUser);
        } catch (NoSuchUserException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
