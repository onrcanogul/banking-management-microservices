package com.devbank.service.transaction.api;

import com.devbank.service.transaction.application.dto.CreateTransferDto;
import com.devbank.service.transaction.application.dto.TransferTransactionDto;
import com.devbank.service.transaction.application.service.TransferService;
import com.template.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {
    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransferTransactionDto>> create(@RequestBody CreateTransferDto model) {
        return ResponseEntity.status(201).body(ApiResponse.ok(service.create(model)));
    }
}
