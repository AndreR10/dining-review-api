package com.example.diningReview.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.example.diningReview.enums.ReviewStatus;
import com.example.diningReview.models.Restaurant;
import com.example.diningReview.models.Review;
import com.example.diningReview.models.UserAccount;
import com.example.diningReview.repositories.RestaurantRepository;
import com.example.diningReview.repositories.ReviewRepository;
import com.example.diningReview.repositories.UserAccountRepository;
import com.example.diningReview.services.RestaurantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    private RestaurantService restaurantService;

    private RestaurantRepository restaurantRepository;

    private ReviewRepository reviewRepository;

    private UserAccountRepository userRepository;

    public ReviewController(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository,
            UserAccountRepository userRepository, RestaurantService restaurantService) {
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/")
    public Iterable<Review> getReviews() {

        return this.reviewRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        return ResponseEntity.ok(review);

    }

    @PostMapping("/")
    public ResponseEntity<Object> createReview(@RequestBody @Valid Review review, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Custom validation logic to collect information about missing fields
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            // Return a response indicating the missing fields
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        // Check if the restaurant exists
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(review.getRestaurantId());
        if (restaurantOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }

        // Check if the user exists
        Optional<UserAccount> userOptional = userRepository.findByName(review.getReviewerName());
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        reviewRepository.save(review);

        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

}
