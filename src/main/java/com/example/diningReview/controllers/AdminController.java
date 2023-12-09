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

import com.example.diningReview.enums.ReviewStatus;
import com.example.diningReview.models.AdminReviewAction;
import com.example.diningReview.models.Restaurant;
import com.example.diningReview.models.Review;
import com.example.diningReview.repositories.RestaurantRepository;
import com.example.diningReview.repositories.ReviewRepository;
import com.example.diningReview.repositories.UserAccountRepository;
import com.example.diningReview.services.RestaurantService;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private RestaurantService restaurantService;

    private RestaurantRepository restaurantRepository;

    private ReviewRepository reviewRepository;

    private UserAccountRepository userRepository;

    public AdminController(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository,
            UserAccountRepository userRepository, RestaurantService restaurantService) {
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.restaurantService = restaurantService;

    }

    // Endpoint for an admin to get the list of all dining reviews with the given
    // state
    @GetMapping("/review/status")
    public ResponseEntity<List<Review>> getReviewsByStatus(@RequestParam ReviewStatus status) {
        List<Review> reviews = reviewRepository.findByStatus(status);
        return ResponseEntity.ok(reviews);
    }

    // Endpoint for an admin to approve or reject a given dining review
    @PutMapping("/review/{reviewId}/")
    public ResponseEntity<Object> updateReviewStatus(
            @PathVariable Long reviewId,
            @RequestBody @Valid AdminReviewAction adminReviewAction,
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
        // Fetch the existing review
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        // Update the review status based on admin action
        if (adminReviewAction.isAccept()) {
            review.setStatus(ReviewStatus.ACCEPTED);
            // Save the updated review
            Review updatedReview = reviewRepository.save(review);
            // Update restaurant scores
            restaurantService.updateRestaurantScores(review.getRestaurantId());
            return ResponseEntity.ok(updatedReview);
        } else {
            review.setStatus(ReviewStatus.REJECTED);
        }

        // Save the updated review
        Review updatedReview = reviewRepository.save(review);
        return ResponseEntity.ok(updatedReview);
    }

}
