package com.devbank.service.account.api;

import com.devbank.service.account.application.dto.AccountStatusHistoryDto;
import com.devbank.service.account.application.service.AccountStatusHistoryService;
import com.template.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/account-history-status")
public class AccountHistoryStatusController {
    private final AccountStatusHistoryService service;

    public AccountHistoryStatusController(AccountStatusHistoryService service) {
        this.service = service;
    }

    @GetMapping("{accountId}")
    public ResponseEntity<ApiResponse<AccountStatusHistoryDto>> get(@PathVariable UUID accountId) {
        return ResponseEntity.status(200).body(ApiResponse.ok(service.getByAccount(accountId)));
    }
}
