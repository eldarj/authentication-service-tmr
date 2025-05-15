
package com.ejahijagic.auth.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtConfig {
    private final Algorithm algorithm = Algorithm.HMAC256("secret");

    public String generateToken(String phoneNumber, String clientId) {
        return JWT.create()
            .withSubject(phoneNumber)
            .withClaim("clientId", clientId)
            .withAudience(clientId)
            .withIssuedAt(new Date())
            .withExpiresAt(Date.from(Instant.now().plusSeconds(3600)))
            .sign(algorithm);
    }
}
