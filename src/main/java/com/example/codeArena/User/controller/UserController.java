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

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserInfo(@PathVariable String email) {
        // 사용자 정보 조회
        Optional<User> user = userService.getUserInfo(email);
        return user.map(u -> ResponseEntity.ok(new UserDto(u.getUsername(), u.getNickname(), u.getEmail()))).orElse(ResponseEntity.notFound().build());
    }
}
