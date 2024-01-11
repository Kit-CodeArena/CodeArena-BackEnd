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
        INVALID_CREDENTIALS("잘못된 이메일 또는 비밀번호입니다."),
        EMAIL_ALREADY_EXISTS("이미 사용 중인 이메일입니다."),
        NICKNAME_ALREADY_EXISTS("이미 사용 중인 닉네임입니다."),
        ;

        private final String message;

        ErrorCode(String message) {
            this.message = message;
        }
    }
}
