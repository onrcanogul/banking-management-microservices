package com.devbank.service.transaction.application.service;

import com.devbank.service.transaction.application.dto.CreatePaymentDto;
import com.devbank.service.transaction.application.dto.PaymentTransactionDto;
import com.devbank.service.transaction.application.event.producer.PaymentInitiatedProcessor;
import com.devbank.service.transaction.domain.entity.PaymentTransaction;
import com.devbank.service.transaction.infrastructure.repository.PaymentTransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.event.transaction.PaymentInitiatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    private final PaymentTransactionRepository repository;
    private final PaymentInitiatedProcessor paymentInitiatedProcessor;
    private final ObjectMapper objectMapper;

    public PaymentService(PaymentTransactionRepository repository, PaymentInitiatedProcessor paymentInitiatedProcessor, ObjectMapper objectMapper) {
        this.repository = repository;
        this.paymentInitiatedProcessor = paymentInitiatedProcessor;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public PaymentTransactionDto create(CreatePaymentDto model) {
        PaymentTransaction paymentTransaction = PaymentTransaction
                .createPayment(model.payerId(), model.merchantId(), model.billRef(), model.currency(), model.amount(), model.externalRef());

        PaymentTransaction createdPaymentTransaction = repository.save(paymentTransaction);

        send(createdPaymentTransaction, model.description());

        return objectMapper.convertValue(createdPaymentTransaction, PaymentTransactionDto.class);
    }

    private void send(PaymentTransaction createdPaymentTransaction, String description) {
        paymentInitiatedProcessor.process(new PaymentInitiatedEvent(
                createdPaymentTransaction.getId(), createdPaymentTransaction.getPayerId(), createdPaymentTransaction.getMerchantId(),
                createdPaymentTransaction.getCurrency(), createdPaymentTransaction.getAmount(), createdPaymentTransaction.getExternalRef(),
                description
        ));
    }
}
