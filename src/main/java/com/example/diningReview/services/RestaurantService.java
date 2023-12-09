package com.example.diningReview.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.diningReview.enums.ReviewStatus;
import com.example.diningReview.models.Restaurant;
import com.example.diningReview.models.Review;
import com.example.diningReview.repositories.RestaurantRepository;
import com.example.diningReview.repositories.ReviewRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public void updateRestaurantScores(Long restaurantId) {
        List<Review> restaurantTotalAcceptedReviews = reviewRepository.findByStatusAndRestaurantId(
                ReviewStatus.ACCEPTED,
                restaurantId);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        // Calculate average scores
        // Total accepted reviews of the restaurant
        int totalAcceptedlReviews = restaurantTotalAcceptedReviews.size();

        // AVG Peanut Score
        List<Review> restaurantPeanutReviews = reviewRepository.findByStatusAndRestaurantIdAndPeanutScoreIsNotNull(
                ReviewStatus.ACCEPTED,
                restaurantId);
        int totalPeanutReviews = restaurantPeanutReviews.size();
        double totalPeanutScore = restaurantPeanutReviews.stream().mapToDouble(Review::getPeanutScore).sum();
        double avgPeanutScore = totalPeanutScore / totalPeanutReviews;

        // AVG Egg Score
        List<Review> restaurantEggReviews = reviewRepository.findByStatusAndRestaurantIdAndEggScoreIsNotNull(
                ReviewStatus.ACCEPTED,
                restaurantId);
        int totalEggReviews = restaurantEggReviews.size();
        double totalEggScore = restaurantEggReviews.stream().mapToDouble(Review::getEggScore).sum();
        double avgEggScore = totalEggScore / totalEggReviews;

        // AVG Dairy Score
        List<Review> restaurantDairyReviews = reviewRepository.findByStatusAndRestaurantIdAndDairyScoreIsNotNull(
                ReviewStatus.ACCEPTED,
                restaurantId);
        int totalDairyReviews = restaurantDairyReviews.size();
        double totalDairyScore = restaurantDairyReviews.stream().mapToDouble(Review::getDairyScore).sum();
        double avgDairyScore = totalDairyScore / totalDairyReviews;

        restaurant.setPeanutScore(avgPeanutScore);
        restaurant.setEggScore(avgEggScore);
        restaurant.setDairyScore(avgDairyScore);
        restaurant.setAvgScore(
                (avgPeanutScore + avgEggScore + avgDairyScore) / totalAcceptedlReviews);

        restaurantRepository.save(restaurant);
    }
}
