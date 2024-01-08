package com.example.codeArena.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotBlank(message = "사용자 이름을 입력해주세요.")
    @Size(min = 2, max = 5, message = "사용자 이름은 2자 이상, 5자 이하여야 합니다.")
    private String username;

    @NotBlank(message = "사용자 닉네임을 입력해주세요.")
    @Size(min = 2, max = 10, message = "사용자 닉네임은 2자 이상, 10자 이하여야 합니다.")
    private String nickname;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;
}