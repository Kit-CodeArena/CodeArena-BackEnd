package com.example.codeArena.exception;

import com.example.codeArena.exception.CustomException.ErrorCode;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status; // 상태 코드
    private final String error; // 상태 코드의 이름
    private final String code; // 설정한 에러 코드
    private final String message; // 설정한 메시지

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(400) // TODO : 상태 코드 받아서 수정하기 (getStatus)
                .body(ErrorResponse.builder()
                        .status(400) // TODO : 상태 코드 받아서 수정하기 (getStatus().value())
                        .error("error") // TODO : 상태 코드 받아서 수정하기 (getStatus().name())
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .build()
                );
    }
}
