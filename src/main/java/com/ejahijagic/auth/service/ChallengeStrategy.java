
package com.ejahijagic.auth.service;

import com.ejahijagic.auth.dto.ChallengeRequest;

public interface ChallengeStrategy {
    boolean supports(String channel);
    void sendChallenge(ChallengeRequest request, String code, String challengeId);
}
