package com.example.feeservice.service;

import com.example.feeservice.dto.FeeDTO;
import com.example.feeservice.dto.PaymentResponseDTO;
import com.example.feeservice.entity.Fee;
import com.example.feeservice.entity.FeeDues;
import com.example.feeservice.repository.FeeDuesRepository;
import com.example.feeservice.repository.FeeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeeServiceImpl implements FeeService {
    @Autowired
    private final FeeRepository feeRepository;
    @Autowired
    private final FeeDuesRepository feeDuesRepository;
    private final WebClient webClient;

    @Value("${payment.service.base-url:http://payment-service}")
    private String paymentServiceBaseUrl;

    public FeeServiceImpl(FeeRepository feeRepository,
                          FeeDuesRepository feeDuesRepository,
                          WebClient.Builder webClientBuilder) {
        this.feeRepository = feeRepository;
        this.feeDuesRepository = feeDuesRepository;
        this.webClient = webClientBuilder.baseUrl(paymentServiceBaseUrl).build();
    }

    @Override
    public FeeDTO createOrUpdateFee(FeeDTO feeDTO) {
        Fee feeEntity = new Fee();
        BeanUtils.copyProperties(feeDTO, feeEntity);
        Fee saved = feeRepository.save(feeEntity);
        FeeDTO response = new FeeDTO();
        BeanUtils.copyProperties(saved, response);
        return response;
    }

    @Override
    public List<FeeDTO> getAllFees() {
        return feeRepository.findAll().stream()
                .map(fee -> {
                    FeeDTO dto = new FeeDTO();
                    BeanUtils.copyProperties(fee, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Double getDueAmount(Long studentId) {
        FeeDues dues = feeDuesRepository.findByStudentId(studentId)
                .orElse(new FeeDues(null, studentId, 0.0, true));
        return dues.getDueAmount();
    }

    @Override
    @CircuitBreaker(name = "paymentCircuitBreaker", fallbackMethod = "paymentFallback")
    public PaymentResponseDTO payFees(Long studentId) {
        FeeDues dues = feeDuesRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("No dues found for student ID: " + studentId));

        if (Boolean.TRUE.equals(dues.getPaid())) {
            return new PaymentResponseDTO(studentId, 0.0, "ALREADY_PAID", null, "Fees already paid");
        }

        Double amountToPay = dues.getDueAmount();
        Mono<PaymentResponseDTO> responseMono = webClient
                .post()
                .uri("/payments/{studentId}", studentId)
                .bodyValue(amountToPay)
                .retrieve()
                .bodyToMono(PaymentResponseDTO.class);

        PaymentResponseDTO paymentResponse = responseMono.block();
        if (paymentResponse != null && "SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {
            dues.setPaid(true);
            dues.setDueAmount(0.0);
            feeDuesRepository.save(dues);
        }
        return paymentResponse;
    }

    // Fallback when Payment Service is unavailable
    public PaymentResponseDTO paymentFallback(Long studentId, Throwable t) {
        return new PaymentResponseDTO(
                studentId,
                0.0,
                "FAILED",
                null,
                "Payment Service unavailable. Please try again later."
        );
    }
}
