package com.example.codeArena.chatting.dto;

import com.example.codeArena.chatting.domain.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChatRoomCreateRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, message = "내용은 1자 이상이여야 합니다.")
    private String name;

    @NotNull(message = "최대 인원을 설정해주세요.")
    @Min(value=1, message = "1 이상의 값을 입력해주세요.")
    @Max(value = 30, message = "30 이하의 값을 입력해주세요.") // 최대 인원은 추후에 수정 가능
    private int maxUserNum;

    @NotNull(message = "태그를 입력해 주세요.")
    private Tag tag;
    // TODO: User 추가
}
