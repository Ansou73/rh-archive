// config/JwtConfig.java
// ════════════════════════════════════════════════════════════
package com.rharchive.config;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Getter
public class JwtConfig {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration; // en millisecondes (ex: 86400000 = 24h)

    /**
     * Clé HMAC-SHA256 dérivée du secret applicatif.
     * Utilisée par JwtUtil pour signer et vérifier les tokens.
     */
    @Bean
    public Key jwtSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /** Date d'expiration calculée depuis maintenant */
    public Date buildExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration);
    }

    /** Durée d'expiration en heures (pour affichage/log) */
    public long getExpirationHeures() {
        return expiration / 3_600_000;
    }
}