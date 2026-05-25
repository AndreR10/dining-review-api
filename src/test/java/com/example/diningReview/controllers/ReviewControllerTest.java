package com.example.diningReview.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.diningReview.enums.ReviewStatus;
import com.example.diningReview.models.Restaurant;
import com.example.diningReview.models.Review;
import com.example.diningReview.models.UserAccount;
import com.example.diningReview.repositories.RestaurantRepository;
import com.example.diningReview.repositories.ReviewRepository;
import com.example.diningReview.repositories.UserAccountRepository;
import com.example.diningReview.services.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Integration tests for ReviewController.
 * Tests HTTP endpoints and request/response handling.
 */
@WebMvcTest(ReviewController.class)
@DisplayName("ReviewController Integration Tests")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantRepository restaurantRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private UserAccountRepository userAccountRepository;

    @MockBean
    private RestaurantService restaurantService;

    private Restaurant testRestaurant;
    private UserAccount testUser;
    private Review testReview;

    @BeforeEach
    void setUp() {
        testRestaurant = new Restaurant();
        testRestaurant.setId(1L);
        testRestaurant.setName("Test Restaurant");
        testRestaurant.setZipCode("12345");

        testUser = new UserAccount();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setCity("Cityville");
        testUser.setState("CA");
        testUser.setZipCode("12345");

        testReview = new Review();
        testReview.setId(1L);
        testReview.setReviewerName("John Doe");
        testReview.setRestaurantId(1L);
        testReview.setPeanutScore(4.0);
        testReview.setEggScore(3.5);
        testReview.setDairyScore(4.5);
        testReview.setCommentary("Great food!");
        testReview.setStatus(ReviewStatus.PENDING);
    }

    @Test
    @DisplayName("GET /api/v1/review/ should return all reviews")
    void testGetAllReviews_ReturnsOkStatus() throws Exception {
        // Arrange
        List<Review> reviews = List.of(testReview);
        when(reviewRepository.findAll()).thenReturn(reviews);

        // Act & Assert
        mockMvc.perform(get("/api/v1/review/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].reviewerName", is("John Doe")));

        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/review/{id} should return specific review")
    void testGetReviewById_ReturnsOkStatus() throws Exception {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(java.util.Optional.of(testReview));

        // Act & Assert
        mockMvc.perform(get("/api/v1/review/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.reviewerName", is("John Doe")));

        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /api/v1/review/{id} should return 404 when review not found")
    void testGetReviewById_ReturnsNotFound() throws Exception {
        // Arrange
        when(reviewRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/review/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/review/ should create review with valid data")
    void testCreateReview_WithValidData_ReturnsCreatedStatus() throws Exception {
        // Arrange
        when(restaurantRepository.findById(1L)).thenReturn(java.util.Optional.of(testRestaurant));
        when(userAccountRepository.findByName("John Doe")).thenReturn(java.util.Optional.of(testUser));
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        // Act & Assert
        mockMvc.perform(post("/api/v1/review/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testReview)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("PENDING")));

        verify(restaurantRepository, times(1)).findById(1L);
        verify(userAccountRepository, times(1)).findByName("John Doe");
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("POST /api/v1/review/ should return 400 when restaurant not found")
    void testCreateReview_RestaurantNotFound_ReturnsBadRequest() throws Exception {
        // Arrange
        when(restaurantRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/v1/review/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testReview)))
                .andExpect(status().isNotFound());

        verify(reviewRepository, never()).save(any());
    }

    @Test
    @DisplayName("POST /api/v1/review/ should return 400 when user not found")
    void testCreateReview_UserNotFound_ReturnsBadRequest() throws Exception {
        // Arrange
        when(restaurantRepository.findById(1L)).thenReturn(java.util.Optional.of(testRestaurant));
        when(userAccountRepository.findByName("John Doe")).thenReturn(java.util.Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/v1/review/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testReview)))
                .andExpect(status().isNotFound());

        verify(reviewRepository, never()).save(any());
    }

    @Test
    @DisplayName("POST /api/v1/review/ should return 400 when required fields missing")
    void testCreateReview_MissingRequiredFields_ReturnsBadRequest() throws Exception {
        // Arrange
        Review invalidReview = new Review();
        invalidReview.setRestaurantId(1L);
        // Missing reviewerName

        // Act & Assert
        mockMvc.perform(post("/api/v1/review/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidReview)))
                .andExpect(status().isBadRequest());

        verify(reviewRepository, never()).save(any());
    }

    @Test
    @DisplayName("POST /api/v1/review/ should return 400 when restaurant ID missing")
    void testCreateReview_MissingRestaurantId_ReturnsBadRequest() throws Exception {
        // Arrange
        Review invalidReview = new Review();
        invalidReview.setReviewerName("John Doe");
        // Missing restaurantId

        // Act & Assert
        mockMvc.perform(post("/api/v1/review/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidReview)))
                .andExpect(status().isBadRequest());

        verify(reviewRepository, never()).save(any());
    }
}
