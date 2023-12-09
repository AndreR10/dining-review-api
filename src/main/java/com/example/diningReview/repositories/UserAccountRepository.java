package com.example.diningReview.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.diningReview.models.UserAccount;

import java.util.Optional;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    public Optional<UserAccount> findByName(String displayName);
}
