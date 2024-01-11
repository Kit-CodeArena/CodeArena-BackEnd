
package com.example.codeArena.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /*
     * TODO
     * 400 BAD_REQUEST : 잘못된 요청
     * 401 UNAUTHORIZED : 인증되지 않은 사용자
     * 404 NOT_FOUND : Resource 를 찾을 수 없음
     * 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재
     * 여러 상태 코드 추가 가능
     *
     * private HttpStatus status; // 상태 코드 받는 부분도 추가할 것.
     */

    @Getter
    public enum ErrorCode {
        USER_ALREADY_EXISTS("사용자가 이미 존재합니다."),
        USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
        INVALID_CREDENTIALS("잘못된 이메일 또는 비밀번호입니다."),
        EMAIL_ALREADY_EXISTS("이미 사용 중인 이메일입니다."),
        NICKNAME_ALREADY_EXISTS("이미 사용 중인 닉네임입니다."),

        INVALID_INPUT_VALUE("잘못된 입력 값 입니다."), // validation error 는 400
        ;

        private final String message;

        ErrorCode(String message) {
            this.message = message;
        }
    }
}
