package com.example.feeservice.controller;

import com.example.feeservice.dto.FeeDTO;
import com.example.feeservice.dto.PaymentResponseDTO;
import com.example.feeservice.service.FeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing fees and initiating payments.
 * Exposes endpoints under /fees for creating/updating fees, retrieving fee structures,
 * checking outstanding dues, and processing payments for a given student.
 */
@RestController
@RequestMapping("/fees")
public class FeeController {
    @Autowired
    private final FeeService feeService;

    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    /**
     * Create or update a fee structure.
     *
     * @param feeDTO The FeeDTO containing grade, amount, and frequency.
     * @return The created or updated FeeDTO.
     */
    @PostMapping("/createOrUpdateFee")
    public ResponseEntity<FeeDTO> createOrUpdateFee(@Valid @RequestBody FeeDTO feeDTO) {
        FeeDTO createdOrUpdated = feeService.createOrUpdateFee(feeDTO);
        return ResponseEntity.ok(createdOrUpdated);
    }

    /**
     * Retrieve all fee structures.
     *
     * @return List of FeeDTO objects representing all fees.
     */
    @GetMapping("/getAllFees")
    public ResponseEntity<List<FeeDTO>> getAllFees() {
        List<FeeDTO> fees = feeService.getAllFees();
        return ResponseEntity.ok(fees);
    }

    /**
     * Check the outstanding due amount for a specific student.
     *
     * @param studentId The ID of the student whose dues are being queried.
     * @return A Double representing the current due amount (0.0 if none).
     */
    @GetMapping("/dues/{studentId}")
    public ResponseEntity<Double> getDueAmount(@PathVariable Long studentId) {
        Double dueAmount = feeService.getDueAmount(studentId);
        return ResponseEntity.ok(dueAmount);
    }

    /**
     * Initiate a fee payment for a specific student.
     * Internally calls the Payment Service and applies a circuit breaker for fault tolerance.
     *
     * @param studentId The ID of the student whose fees are being paid.
     * @return A PaymentResponseDTO containing payment status, transaction ID, and message.
     */
    @PostMapping("/pay/{studentId}")
    public ResponseEntity<PaymentResponseDTO> payFees(@PathVariable Long studentId) {
        PaymentResponseDTO response = feeService.payFees(studentId);
        return ResponseEntity.ok(response);
    }
}
