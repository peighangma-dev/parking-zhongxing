package com.parking.common.core;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final int code;
    private final HttpStatus httpStatus;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BusinessException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.httpStatus = httpStatus;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BusinessException(int code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
