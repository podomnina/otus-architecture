package ru.otus.payment.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.common.error.BusinessAppException;
import ru.otus.lib.kafka.model.PaymentConfirmationModel;
import ru.otus.lib.kafka.model.PaymentProcessModel;
import ru.otus.lib.kafka.service.BusinessTopics;
import ru.otus.lib.kafka.service.KafkaProducerService;
import ru.otus.payment.service.mapper.AccountMapper;
import ru.otus.payment.service.model.dto.AccountResponseDto;
import ru.otus.payment.service.model.dto.FillUpAccountRequestDto;
import ru.otus.payment.service.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private AccountRepository repository;
    private AccountMapper mapper;
    private KafkaProducerService kafkaProducerService;

    public AccountResponseDto get(UUID userId) {
        var account = repository.findById(userId);
        if (account.isEmpty()) {
            log.error("Account for the user with id {} not found", userId);
            throw new BusinessAppException("account.not.found", "Account not found");
        }

        return mapper.map(account.get());
    }

    @Transactional
    public AccountResponseDto fillUp(UUID userId, FillUpAccountRequestDto dto) {
        var accountOpt = repository.findById(userId);
        if (accountOpt.isEmpty()) {
            log.error("Account for the user with id {} not found", userId);
            throw new BusinessAppException("account.not.found", "Account not found");
        }

        var account = accountOpt.get();
        account.setAmount(account.getAmount().add(dto.getAmount())); //todo check
        var updatedAccount = repository.save(account);
        return mapper.map(updatedAccount);
    }

    @Transactional
    public void handlePaymentProcess(PaymentProcessModel model) {
        var userId = model.getUserId();
        var orderId = model.getOrderId();
        var accountOpt = repository.findById(userId);
        if (accountOpt.isEmpty()) {
            log.error("Account for the user with id {} not found", userId);
            throw new BusinessAppException("account.not.found", "Account not found");
        }

        var account = accountOpt.get();
        var currentUserAmount = account.getAmount();
        var amountAfterPayment = currentUserAmount.subtract(model.getAmount());

        if (amountAfterPayment.compareTo(BigDecimal.ZERO) == -1) {
            log.error("Insufficient funds for user with id: {} to pay for the order with id: {}", userId, orderId);
            var errorModel = PaymentConfirmationModel.error(orderId, "Insufficient funds");
            kafkaProducerService.send(BusinessTopics.ORDER_PAYMENT_CONFIRMATION, errorModel);
        }

        account.setAmount(amountAfterPayment);
        repository.save(account);

        var successModel = PaymentConfirmationModel.success(orderId);
        kafkaProducerService.send(BusinessTopics.ORDER_PAYMENT_CONFIRMATION, successModel);
    }
}
