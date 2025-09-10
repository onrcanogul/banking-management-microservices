package com.devbank.service.identity.application.dto;

import java.util.Set;

public record RegisterDto(
        String email,
        String password,
        String phoneNumber,
        String confirmPassword,
        Set<String> roles
)  {}
