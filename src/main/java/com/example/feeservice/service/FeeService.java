package com.example.feeservice.service;

import com.example.feeservice.dto.FeeDTO;
import com.example.feeservice.dto.PaymentResponseDTO;

import java.util.List;

public interface FeeService {
    FeeDTO createOrUpdateFee(FeeDTO feeDTO);
    List<FeeDTO> getAllFees();
    Double getDueAmount(Long studentId);
    PaymentResponseDTO payFees(Long studentId);
}