package com.example.diningReview.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "City is mandatory")
    private String city;
    @NotBlank(message = "State is mandatory")
    private String state;
    @NotBlank(message = "Zip Code is mandatory")
    private String zipCode;

    private Boolean isAdmin;
    @NotNull(message = "Peanut Allergies is mandatory")
    private Boolean interestedInPeanutAllergies;
    @NotNull(message = "Egg Allergies is mandatory")
    private Boolean interestedInEggAllergies;
    @NotNull(message = "Dairy Allergies is mandatory")
    private Boolean interestedInDairyAllergies;

}
