package com.example.codeArena.User.api;

import com.example.codeArena.User.dto.LoginDto;
import com.example.codeArena.User.dto.RegisterDto;
import com.example.codeArena.User.dto.TokenDto;
import com.example.codeArena.User.dto.UserDto;
import com.example.codeArena.User.domain.User;
import com.example.codeArena.User.service.UserService;
import com.example.codeArena.User.util.JwtTokenProvider;
import com.example.codeArena.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    // 사용자 등록 API
    @PostMapping("/register")
    public ResponseEntity<TokenDto> registerUser(@RequestBody @Valid RegisterDto registerDto) {
        User user = userService.registerUser(registerDto);
        String token = tokenProvider.generateToken(user);
        return ResponseEntity.ok(new TokenDto(token));
    }

    // 사용자 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginDto loginDto) {
        Optional<String> jwtToken = userService.loginUser(loginDto.getEmail(), loginDto.getPassword());

        if (jwtToken.isPresent()) {
            TokenDto tokenDto = new TokenDto(jwtToken.get());
            return ResponseEntity.ok(tokenDto);
        } else {
            // 로그인 실패 시 적절한 예외 메시지 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 인증 정보입니다.");
        }
    }

    // 현재 인증된 사용자 정보 조회 API
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserInfo(@AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null) {
            // 현재 사용자가 없는 경우 적절한 예외 메시지 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("현재 사용자 정보가 없습니다.");
        }

        Optional<User> user = userService.getUserInfo(currentUser.getNickname());
        return user.map(u -> ResponseEntity.ok(new UserDto(u.getUsername(), u.getNickname(), u.getEmail(), u.getRole())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserDto("", "", "", null)));
    }
}
