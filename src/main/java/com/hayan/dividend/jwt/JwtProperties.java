package com.hayan.dividend.jwt;

public interface JwtProperties {
    String SECRET = "{}";
    int EXPIRATION_TIME =  3600000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}