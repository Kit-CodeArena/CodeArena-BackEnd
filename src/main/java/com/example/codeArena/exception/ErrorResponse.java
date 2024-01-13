package com.example.codeArena.exception;

import com.example.codeArena.exception.CustomException.ErrorCode;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status; // 상태 코드
    private final String error; // 상태 코드의 이름
    private final String code; // 설정한 에러 코드
    private final String message; // 설정한 메시지

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        HttpStatus httpStatus = errorCode.getStatus(); // ErrorCode에 할당된 실제 상태 코드를 사용

        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResponse.builder()
                        .status(httpStatus.value()) // ErrorCode에 할당된 상태 코드의 값
                        .error(httpStatus.getReasonPhrase()) // ErrorCode에 할당된 상태 코드의 이름
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .build()
                );
    }
}
