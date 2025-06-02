package com.example.feeservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeDTO {
    private Long id;

    @NotBlank(message = "Grade is required")
    private String grade;

    @NotNull(message = "Amount is required")
    private Double amount;

    @NotBlank(message = "Frequency is required")
    private String frequency;
}