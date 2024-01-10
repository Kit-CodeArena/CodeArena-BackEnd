package com.example.codeArena.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Getter
    public enum ErrorCode {
        USER_ALREADY_EXISTS("사용자가 이미 존재합니다."),
        USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
        INVALID_CREDENTIALS("잘못된 인증 정보입니다."),
        ;

        private final String message;

        ErrorCode(String message) {
            this.message = message;
        }
    }
}
