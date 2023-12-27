package com.example.codeArena.User.Controller;

import com.example.codeArena.User.dto.LoginDto;
import com.example.codeArena.User.dto.RegisterDto;
import com.example.codeArena.User.dto.UserDto;
import com.example.codeArena.User.model.User;
import com.example.codeArena.User.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterDto registerDto) {
        // 사용자 등록 처리
        User user = userService.registerUser(registerDto);
        return ResponseEntity.ok(new UserDto(user.getUsername(), user.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginDto loginDto) {
        // 로그인 처리
        Optional<User> user = userService.loginUser(loginDto.getEmail(), loginDto.getPassword());
        return user.map(u -> ResponseEntity.ok("Login successful"))
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserInfo(@PathVariable String email) {
        // 사용자 정보 조회
        Optional<User> user = userService.getUserInfo(email);
        return user.map(u -> ResponseEntity.ok(new UserDto(u.getUsername(), u.getEmail())))
                .orElse(ResponseEntity.notFound().build());
    }
}
