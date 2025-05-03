package ru.otus.payment.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.common.error.BusinessAppException;
import ru.otus.payment.service.model.dto.PaymentResponseDto;
import ru.otus.payment.service.repository.PaymentRepository;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentResponseDto getPayment(Integer orderId) {
        var paymentOpt = repository.findByOrderId(orderId);
        if (paymentOpt.isEmpty()) {
            throw new BusinessAppException("payment.not.found", "Платеж не найден");
        }

        var payment = paymentOpt.get();
        return PaymentResponseDto.builder()
                .orderId(payment.getOrderId())
                .accountId(payment.getAccount().getUserId())
                .amount(payment.getAmount())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
