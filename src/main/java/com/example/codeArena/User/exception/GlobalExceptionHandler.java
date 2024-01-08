package com.example.codeArena.User.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        CustomException.ErrorCode errorCode = ex.getErrorCode();
        // 모든 예외를 BAD_REQUEST 상태 코드로 처리
        return new ResponseEntity<>(errorCode.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 추가적인 예외 핸들러를 정의할 수 있습니다.
}
