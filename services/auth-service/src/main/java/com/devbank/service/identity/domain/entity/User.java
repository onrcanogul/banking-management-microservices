package com.devbank.service.identity.domain.entity;

import com.devbank.service.identity.domain.enumeration.UserStatus;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    private UUID id;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    @ElementCollection
    private Set<String> roles;
    private UserStatus status;
    private LocalDateTime lastLoginAt;
    private int failedLoginAttempts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static User create(String email, String passwordHash, String phoneNumber, Set<String> roles) {
        User u = new User();
        u.email = email;
        u.passwordHash = passwordHash;
        u.phoneNumber = phoneNumber;
        u.roles = roles;
        u.createdAt = LocalDateTime.now();
        u.failedLoginAttempts = 0;
        u.status = UserStatus.ACTIVE;
        return u;
    }
    public void incrementFailedLoginAttempt() {
        this.setFailedLoginAttempts(this.getFailedLoginAttempts() + 1);
    }
}