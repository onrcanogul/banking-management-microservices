package com.devbank.service.ledger.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter @Setter
public class GetAccountByDateDto {
    private String refId;
    private OffsetDateTime date;
}
