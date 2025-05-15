
package com.ejahijagic.auth.controller;

import com.ejahijagic.auth.dto.AuthResponse;
import com.ejahijagic.auth.dto.ChallengeRequest;
import com.ejahijagic.auth.dto.VerifyRequest;
import com.ejahijagic.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/challenge")
    public ResponseEntity<AuthResponse> challenge(@Valid @RequestBody ChallengeRequest request) {
        return ResponseEntity.ok(service.challenge(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verify(@Valid @RequestBody VerifyRequest request) {
        return ResponseEntity.ok(service.verify(request));
    }
}
