package com.example.codeArena.User.controller;

import com.example.codeArena.User.dto.LoginDto;
import com.example.codeArena.User.dto.RegisterDto;
import com.example.codeArena.User.dto.TokenDto;
import com.example.codeArena.User.dto.UserDto;
import com.example.codeArena.User.model.User;
import com.example.codeArena.User.service.UserService;
import com.example.codeArena.User.util.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider; // JwtTokenProvider 추가

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterDto registerDto) {
        User user = userService.registerUser(registerDto);
        String token = tokenProvider.generateToken(user); // 수정: User 객체 전달
        return ResponseEntity.ok(new TokenDto(token)); // 토큰만 반환
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginDto loginDto) {
        Optional<String> jwtToken = userService.loginUser(loginDto.getEmail(), loginDto.getPassword());

        if (jwtToken.isPresent()) {
            TokenDto tokenDto = new TokenDto(); // TokenDto 객체 생성
            tokenDto.setToken(jwtToken.get()); // 토큰 설정
            return ResponseEntity.ok(tokenDto); // TokenDto 객체 반환
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }



    @GetMapping("/{email}")
    public ResponseEntity<?> getUserInfo(@PathVariable String email) {
        // 사용자 정보 조회
        Optional<User> user = userService.getUserInfo(email);
        return user.map(u -> ResponseEntity.ok(new UserDto(u.getUsername(), u.getNickname(), u.getEmail()))).orElse(ResponseEntity.notFound().build());
    }
}
