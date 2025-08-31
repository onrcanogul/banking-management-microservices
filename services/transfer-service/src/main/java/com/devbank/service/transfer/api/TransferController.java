package com.devbank.service.transfer.api;

import com.devbank.service.transfer.application.dto.CreateTransferDto;
import com.devbank.service.transfer.application.dto.TransferDto;
import com.devbank.service.transfer.application.service.TransferService;
import com.template.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {
    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @GetMapping("account/{accountId}")
    public ResponseEntity<ApiResponse<List<TransferDto>>> getByAccount(@PathVariable UUID accountId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByAccount(accountId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransferDto>> create(@RequestBody CreateTransferDto model) {
        return ResponseEntity.status(201).body(ApiResponse.ok(service.create(model)));
    }
}
