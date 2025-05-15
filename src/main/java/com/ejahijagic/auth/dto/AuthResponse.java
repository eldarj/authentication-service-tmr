
package com.ejahijagic.auth.dto;

public record AuthResponse(String status, String message, String challengeId, String jwt) {}
