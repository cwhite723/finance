package com.hayan.dividend.service.impl;

import com.hayan.dividend.domain.User;
import com.hayan.dividend.domain.constant.Role;
import com.hayan.dividend.domain.dto.UserRequest;
import com.hayan.dividend.repository.UserRepository;
import com.hayan.dividend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public void register(UserRequest request) {
        Set<Role> roles = request.roles();
        User user = new User(request.username(), passwordEncoder.encode(request.password()), roles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public Authentication authenticate(UserRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        return authentication;
    }
}