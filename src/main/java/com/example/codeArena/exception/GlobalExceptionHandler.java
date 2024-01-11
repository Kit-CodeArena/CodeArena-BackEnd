package com.example.codeArena.exception;

import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_INPUT_VALUE;

import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        CustomException.ErrorCode errorCode = ex.getErrorCode();
        // 모든 예외를 BAD_REQUEST 상태 코드로 처리
        return new ResponseEntity<>(errorCode.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return ex.getMessage();
    }


    // TODO : 상태 코드가 추가 되면 받아서 처리할 것.
    /*
     * @Valid 에서 error 가 나면 발생
     * @ExceptionHandler 에 이미 MethodArgumentNotValidException 가 구현되어 있어서
     * 동일한 예외 처리를 하게 되면 Ambiguous(모호성) 문제가 발생
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(400) // TODO : 상태 코드 받아서 수정하기 (getStatus)
                .body(ErrorResponse.builder()
                        .status(400) // 수정 필요
                        .error("error") // 수정 필요
                        .code(INVALID_INPUT_VALUE.name())
                        .message(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage())
                        .build());
    }
}
