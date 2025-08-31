package com.devbank.service.ledger.api;

import com.devbank.service.ledger.application.dto.GetAccountByDateDto;
import com.devbank.service.ledger.application.dto.LedgerAccountDto;
import com.devbank.service.ledger.application.services.LedgerAccountService;
import com.devbank.service.ledger.domain.enumeration.LedgerAccountStatus;
import com.template.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ledger-account")
public class LedgerAccountController {
    private final LedgerAccountService service;

    public LedgerAccountController(LedgerAccountService service) {
        this.service = service;
    }

    @GetMapping("ref/{refId}")
    private ResponseEntity<ApiResponse<LedgerAccountDto>> getByReference(@PathVariable String refId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByReferenceId(refId)));
    }

    @PostMapping("refByDate")
    private ResponseEntity<ApiResponse<List<LedgerAccountDto>>> getByReferenceAndDate(@RequestBody GetAccountByDateDto model) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByReferenceIdAndDate(model.getRefId(), model.getDate())));
    }

    @PutMapping("{id}/{status}")
    private ResponseEntity<ApiResponse<LedgerAccountDto>> updateStatus(@PathVariable UUID id, @PathVariable LedgerAccountStatus status) {
        return ResponseEntity.ok(ApiResponse.ok(service.updateStatus(id, status)));
    }
}
