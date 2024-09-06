package com.hayan.dividend.service;

import com.hayan.dividend.domain.dto.UserRequest;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
    String generateToken(Authentication authentication);
    String authenticateAndGenerateToken(UserRequest request);
}
