package com.example.diningReview.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.diningReview.enums.ReviewStatus;
import com.example.diningReview.models.Restaurant;
import com.example.diningReview.models.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    public List<Review> findByStatus(ReviewStatus reviewStatus);

    public List<Review> findByStatusAndRestaurantId(ReviewStatus reviewStatus, Long restaurantId);

    List<Review> findByStatusAndRestaurantIdAndPeanutScoreIsNotNull(ReviewStatus reviewStatus, Long restaurantId);

    List<Review> findByStatusAndRestaurantIdAndEggScoreIsNotNull(ReviewStatus reviewStatus, Long restaurantId);

    List<Review> findByStatusAndRestaurantIdAndDairyScoreIsNotNull(ReviewStatus reviewStatus, Long restaurantId);
}
