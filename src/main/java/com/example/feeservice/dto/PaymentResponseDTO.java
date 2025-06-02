package com.example.feeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private Long studentId;
    private Double amountPaid;
    private String status;         // e.g., "SUCCESS", "FAILED"
    private String transactionId;
    private String message;
}