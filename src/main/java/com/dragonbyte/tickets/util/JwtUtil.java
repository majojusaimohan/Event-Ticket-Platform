package com.dragonbyte.tickets.util;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public final class JwtUtil {

    private JwtUtil() {
        // Private constructor to prevent instantiation
    }

    public static UUID parseUserId(Jwt jwt){
        return UUID.fromString(jwt.getSubject());
    }
}
