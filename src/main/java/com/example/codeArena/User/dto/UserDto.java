package com.example.codeArena.User.dto;

import com.example.codeArena.User.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String nickname;
    private String email;
    private UserRole role;
}
