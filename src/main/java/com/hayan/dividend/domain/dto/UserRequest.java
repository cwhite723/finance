package com.hayan.dividend.domain.dto;

import com.hayan.dividend.domain.constant.Role;

import java.util.Set;

public record UserRequest(String username, String password, Set<Role> roles) {
}
