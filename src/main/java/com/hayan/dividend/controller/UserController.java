package com.hayan.dividend.controller;

import com.hayan.dividend.domain.dto.UserRequest;
import com.hayan.dividend.global.ApplicationResponse;
import com.hayan.dividend.jwt.JwtProperties;
import com.hayan.dividend.service.AuthenticationService;
import com.hayan.dividend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApplicationResponse<Void> register(@RequestBody UserRequest request) {
        userService.register(request);
        return ApplicationResponse.noData();
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody UserRequest request) {
        String jwtToken = authenticationService.authenticateAndGenerateToken(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("success");
    }
}