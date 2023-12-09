package com.example.diningReview.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import com.example.diningReview.models.Restaurant;

import com.example.diningReview.repositories.RestaurantRepository;

@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {
    private RestaurantRepository repository;

    public RestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public Iterable<Restaurant> getRestaurants() {

        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {

        Restaurant restaurant = this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
        return ResponseEntity.ok(restaurant);

    }

    @PostMapping("/")
    public ResponseEntity<Object> createRestaurant(@RequestBody @Valid Restaurant restaurant,
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
        if (repository.findByNameAndZipCode(restaurant.getName(), restaurant.getZipCode()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant already exists");
        }

        repository.save(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(
            @RequestParam String zipCode,
            @RequestParam(required = false) String allergy) {

        List<Restaurant> restaurants;
        if (allergy != null) {

            // Use the appropriate repository based on the specified allergy
            if ("peanut".equalsIgnoreCase(allergy)) {
                restaurants = repository.findByZipCodeAndPeanutScoreIsNotNullOrderByAvgScoreDesc(zipCode);
            } else if ("egg".equalsIgnoreCase(allergy)) {
                restaurants = repository.findByZipCodeAndEggScoreIsNotNullOrderByAvgScoreDesc(zipCode);
            } else if ("dairy".equalsIgnoreCase(allergy)) {
                restaurants = repository.findByZipCodeAndDairyScoreIsNotNullOrderByAvgScoreDesc(zipCode);
            } else {
                // Handle other allergies or scenarios if needed
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid allergy specified");
            }

        } else {
            restaurants = repository.findByZipCode(zipCode);
        }

        return ResponseEntity.ok(restaurants);

    }

}
