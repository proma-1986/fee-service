package com.example.feeservice.repository;

import com.example.feeservice.entity.FeeDues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeeDuesRepository extends JpaRepository<FeeDues, Long> {
    Optional<FeeDues> findByStudentId(Long studentId);
}
