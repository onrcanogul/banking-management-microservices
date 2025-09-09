package com.devbank.service.transaction.application.event.producer;

import com.devbank.service.transaction.domain.entity.PaymentTransaction;
import com.template.messaging.base.processor.Processor;
import com.template.messaging.event.transaction.PaymentInitiatedEvent;
import com.template.starter.outbox.entity.Outbox;
import com.template.starter.outbox.repository.OutboxRepository;
import org.springframework.stereotype.Component;


@Component
public class PaymentInitiatedProcessor implements Processor<PaymentInitiatedEvent> {
    private final OutboxRepository outboxRepository;

    public PaymentInitiatedProcessor(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    @Override
    public void process(PaymentInitiatedEvent event) {
        outboxRepository.save(Outbox.builder()
                .aggregateId(event.getPaymentId().toString())
                .aggregateType(PaymentTransaction.class.getTypeName())
                .type(event.getClass().getTypeName())
                .destination("payment.initiated")
                .build());
    }
}
