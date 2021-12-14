package com.cgi.dentistapp.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs"; // FIXME: not a good place for secrets
    public static final long EXPIRATION_TIME = 432_000_000; // 5 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long PASSWORD_RESET_TOKEN_EXPIRATION_TIME = 7_200_000; // 2 hours
}

