package com.example.diningReview.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.diningReview.models.UserAccount;
import com.example.diningReview.repositories.UserAccountRepository;

import jakarta.validation.Valid;

import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user-account")
public class UserAccountController {

    private UserAccountRepository repository;

    public UserAccountController(UserAccountRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public Iterable<UserAccount> getAllUsersAccounts() {

        return this.repository.findAll();
    }

    @GetMapping("/{name}")
    public ResponseEntity<UserAccount> getUserAccountByName(@PathVariable String name) {

        UserAccount userAccount = this.repository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(userAccount);

    }

    @PostMapping("/")
    public ResponseEntity<Object> createUserAccount(@RequestBody @Valid UserAccount userAccount,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Custom validation logic to collect information about missing fields
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            // Return a response indicating the missing fields
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        // Check if the display name is unique
        if (repository.findByName(userAccount.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name already in use");
        }

        repository.save(userAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(userAccount);
    }

    @PutMapping("/{name}")
    public ResponseEntity<UserAccount> updateUserAccount(@PathVariable String name,
            @RequestBody @Valid UserAccount updatedUser) {
        // Fetch the existing user
        UserAccount existingUser = repository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Update the user profile (excluding the display name)
        if (updatedUser.getCity() != existingUser.getCity()) {
            existingUser.setCity(updatedUser.getCity());
        }
        existingUser.setCity(updatedUser.getCity());
        existingUser.setState(updatedUser.getState());
        existingUser.setZipCode(updatedUser.getZipCode());
        existingUser.setInterestedInPeanutAllergies(updatedUser.getInterestedInPeanutAllergies());
        existingUser.setInterestedInEggAllergies(updatedUser.getInterestedInEggAllergies());
        existingUser.setInterestedInDairyAllergies(updatedUser.getInterestedInDairyAllergies());

        repository.save(existingUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(existingUser);
    }

}
