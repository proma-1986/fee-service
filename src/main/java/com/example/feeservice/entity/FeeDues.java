package com.example.feeservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fee_dues")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeDues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;        // Reference to StudentService’s student ID
    private Double dueAmount;      // Outstanding amount
    private Boolean paid;          // true iff fully paid
}
