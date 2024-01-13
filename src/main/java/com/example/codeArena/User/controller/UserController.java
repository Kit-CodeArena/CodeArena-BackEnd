package com.example.codeArena.User.controller;

import com.example.codeArena.User.dto.LoginDto;
import com.example.codeArena.User.dto.RegisterDto;
import com.example.codeArena.User.dto.TokenDto;
import com.example.codeArena.User.dto.UserDto;
import com.example.codeArena.User.model.User;
import com.example.codeArena.User.service.UserService;
import com.example.codeArena.User.util.JwtTokenProvider;
import com.example.codeArena.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/register")
    public ResponseEntity<TokenDto> registerUser(@RequestBody @Valid RegisterDto registerDto) {
        User user = userService.registerUser(registerDto);
        String token = tokenProvider.generateToken(user);
        return ResponseEntity.ok(new TokenDto(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginDto loginDto) {
        Optional<String> jwtToken = userService.loginUser(loginDto.getEmail(), loginDto.getPassword());

        if (jwtToken.isPresent()) {
            TokenDto tokenDto = new TokenDto();
            tokenDto.setToken(jwtToken.get());
            return ResponseEntity.ok(tokenDto);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    // 토큰을 사용하여 현재 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUserInfo(@AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null) {
            System.out.println("Current user is null."); // 추가한 로그
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> user = userService.getUserInfo(currentUser.getNickname());
        if (user.isPresent()) {
            UserDto userDto = new UserDto(user.get().getUsername(), user.get().getNickname(), user.get().getEmail());
            return ResponseEntity.ok(userDto);
        } else {
            System.out.println("User not found."); // 추가한 로그
            return ResponseEntity.notFound().build();
        }
    }
}
