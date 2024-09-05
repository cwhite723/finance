package com.hayan.dividend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {
    // 400 Bad Request
    REQUEST_VALIDATION_FAIL(BAD_REQUEST, "잘못된 요청 값입니다."),
    COMPANY_ALREADY_EXISTS(BAD_REQUEST, "회사가 이미 존재합니다."),

    // 401 Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),

    // 403 Forbidden

    // 404 Not Found
    COMPANY_NOT_FOUND(NOT_FOUND, "존재하지 않는 회사입니다."),
    USER_NOT_FOUND(NOT_FOUND, "존재하지 않는 회원입니다."),

    // 409 Conflict

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
