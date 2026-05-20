// controller/AuthController.java
package com.rharchive.controller;

import com.rharchive.dto.request.LoginRequest;
import com.rharchive.dto.response.AuthResponse;
import com.rharchive.entity.Utilisateur;
import com.rharchive.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getMotDePasse()));
        Utilisateur user = (Utilisateur) auth.getPrincipal();
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, "Bearer",
            user.getNom(), user.getEmail(), user.getRole().name()));
    }

    @GetMapping("/me")
    public ResponseEntity<Utilisateur> me(Authentication auth) {
        return ResponseEntity.ok((Utilisateur) auth.getPrincipal());
    }
}