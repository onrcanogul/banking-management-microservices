package com.devbank.service.account.api;

import com.devbank.service.account.application.dto.AccountDto;
import com.devbank.service.account.application.dto.UpdateStatusDto;
import com.devbank.service.account.application.service.AccountService;
import com.template.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/account")
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<AccountDto>> get(@PathVariable UUID id) {
        return ResponseEntity.status(200).body(ApiResponse.ok(service.getById(id)));
    }

    @GetMapping("customer/{customerId}")
    public ResponseEntity<ApiResponse<AccountDto>> getByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.status(200).body(ApiResponse.ok(service.getByCustomer(customerId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountDto>> create(@RequestBody AccountDto model) {
        return ResponseEntity.status(201).body(ApiResponse.ok(service.create(model)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<AccountDto>> update(AccountDto model) {
        return ResponseEntity.status(200).body(ApiResponse.ok(service.update(model)));
    }

    @PutMapping("status")
    public ResponseEntity<ApiResponse<AccountDto>> updateStatus(@RequestBody UpdateStatusDto model) {
        return ResponseEntity.status(200).body(ApiResponse.ok(service.updateStatus(model)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.status(200).body(ApiResponse.ok());
    }

}
