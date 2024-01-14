package com.example.codeArena.chatroom.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;

public enum ChatRoomStatus {
    OPEN("모집 중"),
    CLOSE("모집 종료"),
    FINISH("종료");

    private final String status;

    ChatRoomStatus(String status) {
        this.status = status;
    }

    // JSON to ENUM
    @JsonCreator
    public static ChatRoomStatus of(String statusName) {
        return Arrays.stream(ChatRoomStatus.values())
                .filter(status -> Objects.equals(status.getStatus(), statusName))
                .findFirst()
                .orElseThrow();
    }

    // ENUM to JSON
    @JsonValue
    public String getStatus() {
        return status;
    }
}
