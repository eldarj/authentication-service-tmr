
package com.ejahijagic.auth.dto;

public record VerifyRequest(
    String phoneNumber,
    String otp,
    String challengeId
) {}
