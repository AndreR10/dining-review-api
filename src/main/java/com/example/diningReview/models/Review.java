package com.example.diningReview.models;

import com.example.diningReview.enums.ReviewStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @NotBlank(message = "Name is mandatory")
    private String reviewerName;
    @NotNull(message = "Restaurant ID is mandatory")
    private Long restaurantId;

    private Double peanutScore;

    private Double eggScore;

    private Double dairyScore;
    private String commentary;
    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.PENDING;

}
