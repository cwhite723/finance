package com.hayan.dividend.service.impl;

import com.hayan.dividend.domain.dto.UserRequest;
import com.hayan.dividend.jwt.JwtTokenProvider;
import com.hayan.dividend.service.AuthenticationService;
import com.hayan.dividend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @Override
    public String generateToken(Authentication authentication) {
        Set<String> roles = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toSet());

        return tokenProvider.generateToken(authentication.getName(), roles);
    }

    @Override
    public String authenticateAndGenerateToken(UserRequest request) {
        Authentication authentication = userService.authenticate(request);
        return generateToken(authentication);
    }
}