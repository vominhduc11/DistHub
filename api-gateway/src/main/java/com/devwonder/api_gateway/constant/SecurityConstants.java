package com.devwonder.api_gateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstants {
    
    // Headers
    public static final String USER_ID_HEADER = "X-User-Id";
    public static final String USER_ROLE_HEADER = "X-User-Role";
    public static final String REQUEST_ID_HEADER = "X-Request-Id";
    
    // JWT
    public static final String EXPECTED_ISSUER = "nexhub-auth-service";
    
    // Rate Limiting
    public static final int MAX_REQUESTS_PER_MINUTE = 60;
    public static final long WINDOW_SIZE_MILLIS = 60 * 1000; // 1 minute
}