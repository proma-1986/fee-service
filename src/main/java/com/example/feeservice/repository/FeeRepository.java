package com.example.feeservice.repository;

import com.example.feeservice.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {
    Optional<Fee> findByGrade(String grade);
}

