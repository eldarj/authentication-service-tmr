package com.ejahijagic.auth.service;

import com.ejahijagic.auth.config.JwtConfig;
import com.ejahijagic.auth.dto.AuthResponse;
import com.ejahijagic.auth.dto.ChallengeRequest;
import com.ejahijagic.auth.dto.VerifyRequest;
import com.ejahijagic.auth.repository.UserRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private final List<ChallengeStrategy> strategies;
    private final StringRedisTemplate redis;
    private final JwtConfig jwtConfig;
    private final RateLimitService rateLimit;
    private final UserRepository userRepository;

    private static final Duration TTL = Duration.ofMinutes(5);

    public AuthService(List<ChallengeStrategy> strategies, StringRedisTemplate redis,
                       JwtConfig jwtConfig, RateLimitService rateLimit, UserRepository userRepository) {
        this.strategies = strategies;
        this.redis = redis;
        this.jwtConfig = jwtConfig;
        this.rateLimit = rateLimit;
        this.userRepository = userRepository;
    }

    public AuthResponse challenge(ChallengeRequest req) {
        if (!rateLimit.isAllowed(req.phoneNumber())) {
            long retryAfter = rateLimit.getTtl(req.phoneNumber());
            return new AuthResponse("RATE_LIMIT", "Too many attempts. Try again later.", null, null);
        }

        String otpKey = "otp:" + req.phoneNumber();
        String existing = redis.opsForValue().get(otpKey);
        String otp = (existing != null) ? existing : generateOtp();

        if (existing == null) {
            redis.opsForValue().set(otpKey, otp, TTL);
        }

        String challengeId = UUID.randomUUID().toString();
        redis.opsForValue().set("challenge:" + req.phoneNumber(), challengeId, TTL);

        for (ChallengeStrategy strategy : strategies) {
            if (strategy.supports("SMS") || strategy.supports("PN")) {
                sendAsync(strategy, req, otp, challengeId);
            }
        }

        return new AuthResponse("SENT", "OTP sent", challengeId, null);
    }

    public AuthResponse verify(VerifyRequest req) {
        String expected = redis.opsForValue().get("otp:" + req.phoneNumber());
        String actualChallengeId = redis.opsForValue().get("challenge:" + req.phoneNumber());

        if (expected == null || !expected.equals(req.otp()) || !req.challengeId().equals(actualChallengeId)) {
            return new AuthResponse("INVALID", "OTP or Challenge ID is incorrect or expired", null, null);
        }

        redis.delete("otp:" + req.phoneNumber());
        redis.delete("challenge:" + req.phoneNumber());
        userRepository.saveOrUpdateLogin(req.phoneNumber());

        String jwt = jwtConfig.generateToken(req.phoneNumber(), "default-client");
        return new AuthResponse("SUCCESS", "OTP verified", req.challengeId(), jwt);
    }

    @Async
    public void sendAsync(ChallengeStrategy strategy, ChallengeRequest request, String otp, String challengeId) {
        strategy.sendChallenge(request, otp, challengeId);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}