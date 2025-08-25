package com.devbank.service.account.api;

import com.devbank.service.account.application.dto.AccountLimitDto;
import com.devbank.service.account.application.service.AccountLimitService;
import com.template.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/account-limit")
public class AccountLimitController {
    private final AccountLimitService service;

    public AccountLimitController(AccountLimitService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<AccountLimitDto>> get(@PathVariable UUID id) {
        return ResponseEntity.status(200).body(ApiResponse.ok(service.getById(id)));
    }

    @GetMapping("account/{accountId}")
    public ResponseEntity<ApiResponse<AccountLimitDto>> getByAccount(@PathVariable UUID accountId) {
        return ResponseEntity.status(200).body(ApiResponse.ok(service.getByAccount(accountId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountLimitDto>> create(AccountLimitDto model) {
        return ResponseEntity.status(201).body(ApiResponse.ok(service.create(model)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<AccountLimitDto>> update(AccountLimitDto model) {
        return ResponseEntity.status(200).body(ApiResponse.ok(service.update(model)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.status(200).body(ApiResponse.ok());
    }

}
