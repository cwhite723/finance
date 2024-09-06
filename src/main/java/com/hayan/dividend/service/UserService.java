package com.hayan.dividend.service;

import com.hayan.dividend.domain.dto.UserRequest;
import org.springframework.security.core.Authentication;

public interface UserService {
    void register(UserRequest request);
    Authentication authenticate(UserRequest request);
}
