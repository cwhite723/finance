package com.hayan.dividend.domain.constant;

import lombok.Getter;

@Getter
public enum Role {
    READ,
    WRITE;

    public String getRoleName() {
        return "ROLE_" + name();
    }
}
