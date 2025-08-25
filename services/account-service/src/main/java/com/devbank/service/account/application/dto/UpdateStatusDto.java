package com.devbank.service.account.application.dto;

import com.devbank.service.account.domain.enumeration.AccountStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateStatusDto {
    private UUID id;
    private AccountStatus status;
    private String reason;
}
