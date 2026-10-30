package org.mrp.model;

import lombok.Data;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Data
public class Session {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TOKEN_BYTES = 32;

    private final String token;
    private final UUID userId;
    private final Instant createdAt;
    private final Duration ttl;
    private Instant lastUsedAt;

    public Session(UUID userId, Duration ttl) {
        if (ttl == null || ttl.isNegative() || ttl.isZero()) {
            throw new IllegalArgumentException("ttl must be positive");
        }
        this.token = generateToken();
        this.userId = userId;
        this.createdAt = Instant.now();
        this.ttl = ttl;
        this.lastUsedAt = createdAt;
    }

    private static String generateToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public Instant getExpiresAt() {
        return lastUsedAt.plus(ttl);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(getExpiresAt());
    }

    public void refresh() {
        this.lastUsedAt = Instant.now();
    }
}
