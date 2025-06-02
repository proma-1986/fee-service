package com.example.feeservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String grade;          // e.g., "Grade 1", "Grade 2"
    private Double amount;         // Fee amount for this grade
    private String frequency;      // e.g., "ANNUAL", "MONTHLY"
}
