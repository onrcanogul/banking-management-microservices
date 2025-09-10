package com.devbank.service.identity.application.service;

import com.devbank.service.identity.application.dto.LoginDto;
import com.devbank.service.identity.application.dto.RegisterDto;
import com.devbank.service.identity.application.dto.TokenDto;
import com.devbank.service.identity.domain.entity.RefreshToken;
import com.devbank.service.identity.domain.entity.User;
import com.devbank.service.identity.infrastructure.repository.RefreshTokenRepository;
import com.devbank.service.identity.infrastructure.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public TokenDto login(LoginDto model) {
        User user = validateLoginCredentials(model);
        user.setLastLoginAt(LocalDateTime.now());
        String token = jwtService.generateToken(user, 15);
        String refreshToken = jwtService.generateRefreshToken(user, 7); //stateful -> handle with key partition
        refreshTokenRepository.save(new RefreshToken(UUID.randomUUID(), refreshToken, LocalDateTime.now().plusDays(7), user));
        userRepository.save(user);
        return new TokenDto(token, refreshToken);
    }

    public void register(RegisterDto model) {
        validateRegisterCredentials(model);
        String hashedPassword = passwordEncoder.encode(model.password());
        Set<String> roles = model.roles() == null || model.roles().isEmpty() ? Set.of("CUSTOMER") : model.roles();
        userRepository.save(User.create(model.email(), hashedPassword, model.phoneNumber(), roles));
    }

    private User validateLoginCredentials(LoginDto model) {
        User user = userRepository.findByEmail(model.email())
                .orElseThrow(() -> new RuntimeException(model.email()));

        if (!passwordEncoder.matches(model.password(), user.getPasswordHash())) {
            user.incrementFailedLoginAttempt();
            userRepository.save(user);
            throw new RuntimeException();
        }
        return user;
    }

    private void validateRegisterCredentials(RegisterDto model) {
        if (userRepository.findByEmail(model.email()).isPresent()) {
            throw new RuntimeException("User Already Exist"); //todo domain exception
        }
    }

}
