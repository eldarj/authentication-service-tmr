
package com.ejahijagic.auth.domain;

import java.time.Instant;

public record User(String phoneNumber, Instant lastLogin) {}
