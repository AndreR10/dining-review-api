package com.example.diningReview.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.diningReview.enums.ReviewStatus;
import com.example.diningReview.models.Restaurant;
import com.example.diningReview.models.Review;
import com.example.diningReview.repositories.RestaurantRepository;
import com.example.diningReview.repositories.ReviewRepository;

/**
 * Unit tests for RestaurantService.
 * Tests business logic for restaurant score calculations.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RestaurantService Unit Tests")
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant testRestaurant;
    private Review testReview1;
    private Review testReview2;

    @BeforeEach
    void setUp() {
        testRestaurant = new Restaurant();
        testRestaurant.setId(1L);
        testRestaurant.setName("Test Restaurant");
        testRestaurant.setZipCode("12345");

        testReview1 = new Review();
        testReview1.setId(1L);
        testReview1.setRestaurantId(1L);
        testReview1.setPeanutScore(4.0);
        testReview1.setEggScore(3.5);
        testReview1.setDairyScore(4.5);
        testReview1.setStatus(ReviewStatus.ACCEPTED);

        testReview2 = new Review();
        testReview2.setId(2L);
        testReview2.setRestaurantId(1L);
        testReview2.setPeanutScore(5.0);
        testReview2.setEggScore(4.5);
        testReview2.setDairyScore(5.0);
        testReview2.setStatus(ReviewStatus.ACCEPTED);
    }

    @Test
    @DisplayName("updateRestaurantScores should calculate average scores correctly")
    void testUpdateRestaurantScores_CalculatesAverageCorrectly() {
        // Arrange
        List<Review> allReviews = List.of(testReview1, testReview2);
        List<Review> peanutReviews = List.of(testReview1, testReview2);
        List<Review> eggReviews = List.of(testReview1, testReview2);
        List<Review> dairyReviews = List.of(testReview1, testReview2);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(testRestaurant));
        when(reviewRepository.findByStatusAndRestaurantId(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(allReviews);
        when(reviewRepository.findByStatusAndRestaurantIdAndPeanutScoreIsNotNull(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(peanutReviews);
        when(reviewRepository.findByStatusAndRestaurantIdAndEggScoreIsNotNull(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(eggReviews);
        when(reviewRepository.findByStatusAndRestaurantIdAndDairyScoreIsNotNull(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(dairyReviews);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(testRestaurant);

        // Act
        restaurantService.updateRestaurantScores(1L);

        // Assert
        verify(restaurantRepository).findById(1L);
        verify(restaurantRepository).save(any(Restaurant.class));
        assertEquals(4.5, testRestaurant.getPeanutScore(), 0.01);
        assertEquals(4.0, testRestaurant.getEggScore(), 0.01);
        assertEquals(4.75, testRestaurant.getDairyScore(), 0.01);
    }

    @Test
    @DisplayName("updateRestaurantScores should throw EntityNotFoundException when restaurant not found")
    void testUpdateRestaurantScores_RestaurantNotFound_ThrowsException() {
        // Arrange
        when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> {
            restaurantService.updateRestaurantScores(999L);
        });

        verify(restaurantRepository).findById(999L);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateRestaurantScores should handle single review")
    void testUpdateRestaurantScores_WithSingleReview() {
        // Arrange
        List<Review> singleReview = List.of(testReview1);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(testRestaurant));
        when(reviewRepository.findByStatusAndRestaurantId(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(singleReview);
        when(reviewRepository.findByStatusAndRestaurantIdAndPeanutScoreIsNotNull(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(singleReview);
        when(reviewRepository.findByStatusAndRestaurantIdAndEggScoreIsNotNull(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(singleReview);
        when(reviewRepository.findByStatusAndRestaurantIdAndDairyScoreIsNotNull(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(singleReview);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(testRestaurant);

        // Act
        restaurantService.updateRestaurantScores(1L);

        // Assert
        verify(restaurantRepository).save(any(Restaurant.class));
        assertEquals(4.0, testRestaurant.getPeanutScore(), 0.01);
        assertEquals(3.5, testRestaurant.getEggScore(), 0.01);
        assertEquals(4.5, testRestaurant.getDairyScore(), 0.01);
    }

    @Test
    @DisplayName("updateRestaurantScores should handle empty reviews list")
    void testUpdateRestaurantScores_WithEmptyReviews() {
        // Arrange
        List<Review> emptyReviews = List.of();

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(testRestaurant));
        when(reviewRepository.findByStatusAndRestaurantId(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(emptyReviews);
        when(reviewRepository.findByStatusAndRestaurantIdAndPeanutScoreIsNotNull(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(emptyReviews);
        when(reviewRepository.findByStatusAndRestaurantIdAndEggScoreIsNotNull(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(emptyReviews);
        when(reviewRepository.findByStatusAndRestaurantIdAndDairyScoreIsNotNull(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(emptyReviews);

        // Act & Assert
        // Should handle gracefully - divide by zero or empty list
        assertThrows(ArithmeticException.class, () -> {
            restaurantService.updateRestaurantScores(1L);
        });
    }

    @Test
    @DisplayName("updateRestaurantScores should call save with correct Restaurant object")
    void testUpdateRestaurantScores_CallsSaveWithCorrectObject() {
        // Arrange
        List<Review> reviews = List.of(testReview1);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(testRestaurant));
        when(reviewRepository.findByStatusAndRestaurantId(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(reviews);
        when(reviewRepository.findByStatusAndRestaurantIdAndPeanutScoreIsNotNull(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(reviews);
        when(reviewRepository.findByStatusAndRestaurantIdAndEggScoreIsNotNull(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(reviews);
        when(reviewRepository.findByStatusAndRestaurantIdAndDairyScoreIsNotNull(ReviewStatus.ACCEPTED, 1L))
                .thenReturn(reviews);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(testRestaurant);

        // Act
        restaurantService.updateRestaurantScores(1L);

        // Assert
        verify(restaurantRepository, times(1)).save(argThat(restaurant ->
                restaurant.getId().equals(1L) &&
                restaurant.getPeanutScore().equals(4.0)
        ));
    }
}
