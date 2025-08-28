package com.template.messaging.event.account.process;

import com.template.messaging.event.Event;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
public class AccountCreatedEvent implements Event {
    private UUID accountId;
    private String currency;
}
