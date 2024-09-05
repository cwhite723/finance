package com.hayan.dividend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.hayan.dividend.exception.ErrorCode.REQUEST_VALIDATION_FAIL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.from(errorCode);

        logger.error("CustomException: {}, HTTP Status: {}", e.getMessage(), errorCode.getHttpStatus());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(RuntimeException e) {

        ErrorResponse response = ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR);

        logger.error("Unexpected Exception: {}", e.getMessage(), e);

        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String requestURI = request.getRequestURI();

        ErrorResponse errorResponse = ErrorResponse.from(REQUEST_VALIDATION_FAIL);

        logger.error("MethodArgumentNotValidException: {}, Request URI: {}", errorMessage, requestURI);

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
        String errorMessage = e.getMessage();
        String requestURI = request.getRequestURI();

        ErrorResponse errorResponse = ErrorResponse.from(REQUEST_VALIDATION_FAIL);

        logger.error("ConstraintViolationException: {}, Request URI: {}", errorMessage, requestURI);

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }
}
