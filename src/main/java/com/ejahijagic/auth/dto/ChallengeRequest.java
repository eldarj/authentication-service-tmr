
package com.ejahijagic.auth.dto;

public record ChallengeRequest(
    String phoneNumber,
    String locale,
    String clientId,
    String deviceId,
    String ipAddress
) {}
