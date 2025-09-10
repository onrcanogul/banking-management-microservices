package com.devbank.service.identity.api;

import com.devbank.service.identity.application.dto.LoginDto;
import com.devbank.service.identity.application.dto.RegisterDto;
import com.devbank.service.identity.application.dto.TokenDto;
import com.devbank.service.identity.application.service.AuthService;
import com.template.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<TokenDto>> login(@RequestBody LoginDto model) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(model)));
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterDto model) {
        authService.register(model);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
