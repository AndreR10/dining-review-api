package com.example.diningReview.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.diningReview.models.Restaurant;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    public Optional<Restaurant> findByNameAndZipCode(String name, String zipCode);

    public List<Restaurant> findByZipCode(String zipCode);

    List<Restaurant> findByZipCodeAndPeanutScoreIsNotNullOrderByAvgScoreDesc(String zipCode);

    List<Restaurant> findByZipCodeAndEggScoreIsNotNullOrderByAvgScoreDesc(String zipCode);

    List<Restaurant> findByZipCodeAndDairyScoreIsNotNullOrderByAvgScoreDesc(String zipCode);

}
