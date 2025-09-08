package com.devbank.service.ledger.api;

import com.devbank.service.ledger.application.dto.LedgerEntryDto;
import com.devbank.service.ledger.application.service.LedgerEntryService;
import com.template.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ledger-entry")
public class LedgerEntryController {
    private final LedgerEntryService service;

    public LedgerEntryController(LedgerEntryService service) {
        this.service = service;
    }

    @GetMapping("{externalRefId}")
    public ResponseEntity<ApiResponse<List<LedgerEntryDto>>> get(@PathVariable String externalRefId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByExternalRefId(externalRefId)));
    }

    @GetMapping("expenses/{externalRefId}")
    public ResponseEntity<ApiResponse<List<LedgerEntryDto>>> getExpenses(@PathVariable String externalRefId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getExpenses(externalRefId)));
    }

    @GetMapping("credits/{externalRefId}")
    public ResponseEntity<ApiResponse<List<LedgerEntryDto>>> getCredits(@PathVariable String externalRefId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getCredits(externalRefId)));
    }
}
