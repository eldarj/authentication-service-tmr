
package com.ejahijagic.auth.service;

import com.ejahijagic.auth.dto.ChallengeRequest;
import org.springframework.stereotype.Component;

@Component
public class SmsChallengeStrategy implements ChallengeStrategy {

    private final NotificationPublisher publisher;

    public SmsChallengeStrategy(NotificationPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public boolean supports(String channel) {
        return "SMS".equalsIgnoreCase(channel);
    }

    @Override
    public void sendChallenge(ChallengeRequest request, String code, String challengeId) {
        String message = code + " is your OTP code to login into Tamara. Do not share this code.";
        publisher.publishSms(request.phoneNumber(), message);
    }
}
