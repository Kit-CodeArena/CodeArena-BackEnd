package com.example.codeArena.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus status;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.status = errorCode.getStatus();
    }

    /*
     * TODO
     * 400 BAD_REQUEST : 잘못된 요청
     * 401 UNAUTHORIZED : 인증되지 않은 사용자
     * 404 NOT_FOUND : Resource 를 찾을 수 없음
     * 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재
     * 여러 상태 코드 추가 가능
     */

    @Getter
    public enum ErrorCode {
        // 각 에러 유형에 맞는 HTTP 상태 코드 할당
        // 400 BAD_REQUEST : 잘못된 요청
        INVALID_INPUT_VALUE("잘못된 입력 값 입니다.", HttpStatus.BAD_REQUEST),
        INVALID_INPUT_VALUE_DTO("dto 매핑 변수가 맞지않음", HttpStatus.BAD_REQUEST),
        CHAT_ROOM_USER_ILLEGAL_ROLE("올바르지 않는 채팅 방 유저의 권한입니다.", HttpStatus.BAD_REQUEST),
        IS_NOT_OPENING("'모집 중' 상태가 아닙니다.", HttpStatus.BAD_REQUEST),
        IS_NOT_CLOSE("'모집 종료' 상태가 아닙니다.", HttpStatus.BAD_REQUEST),
        INVALID_STRATEGY("잘못된 전략 입니다.", HttpStatus.BAD_REQUEST),
        WEB_SOCKET_SA_NULL("SessionAttributes가 null입니다.", HttpStatus.BAD_REQUEST),
        INVALID_PROPOSAL_STATUS("잘못된 신청서 상태입니다.", HttpStatus.BAD_REQUEST),


        // 401 UNAUTHORIZED : 인증되지 않은 사용자
        INVALID_CREDENTIALS("잘못된 이메일 또는 비밀번호입니다.", HttpStatus.UNAUTHORIZED),
        INVALID_CONTEXT("Security Context 에 인증 정보가 없습니다.", HttpStatus.UNAUTHORIZED),
        JWT_VALIDATION_FAILED("JWT 토큰 검증 실패", HttpStatus.UNAUTHORIZED),


        // 403
        ACCESS_DENIED("접근이 거부되었습니다.", HttpStatus.FORBIDDEN),

        // 404 NOT_FOUND : Resource 를 찾을 수 없음
        VALUE_NOT_FOUNE("값을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
        USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
        PROBLEM_NOT_FOUND("문제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
        POST_NOT_FOUND("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
        COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
        CHAT_ROOM_NOT_FOUND("채팅방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
        CHAT_ROOM_USER_NOT_FOUND("채팅방에서 해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
        AUTH_HEADER_NOT_FOUND("authHeaderValue가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
        PROPOSAL_NOT_FOUND("해당 신청서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

        // 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재
        EMAIL_ALREADY_EXISTS("이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
        NICKNAME_ALREADY_EXISTS("이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT),
        USER_ALREADY_EXISTS("사용자가 이미 존재합니다.", HttpStatus.CONFLICT),
        CHAT_ROOM_ALREADY_EXIST("이미 해당 사용자와 관련된 방이 존재합니다.", HttpStatus.CONFLICT),
        PROPOSAL_ALREADY_EXIST("이미 신청서가 존재합니다.", HttpStatus.CONFLICT),
        CHAT_ROOM_USER_COUNT_OVER_CAPACITY("해당 방이 가득찼습니다.", HttpStatus.CONFLICT),

        // 채팅방 사용자 중복
        DUPLICATE_BLOCKED_USER_FROM_CHAT_RROM("이미 해당 방에 사용자가 존재하며, 차단된 사용자입니다.",  HttpStatus.CONFLICT),
        DUPLICATE_LEADER_USER_FROM_CHAT_RROM("이미 해당 방에 사용자가 존재하며, 방의 리더입니다.", HttpStatus.CONFLICT),
        DUPLICATE_USER_FROM_CHAT_ROOM("이미 해당 방에 사용자가 존재하며, 방의 유저입니다.", HttpStatus.CONFLICT),

        // 500
        IMAGE_PROCESSING_FAILED("이미지 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),;

        private final String message;
        @Getter
        private final HttpStatus status;

        ErrorCode(String message, HttpStatus status) {
            this.message = message;
            this.status = status;
        }
        }
}