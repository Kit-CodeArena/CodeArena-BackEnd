package com.example.codeArena.User.service;

import com.example.codeArena.User.dto.RegisterDto;
import com.example.codeArena.User.exception.CustomException;
import com.example.codeArena.User.model.User;
import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.User.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider; // JwtTokenProvider 추가

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider; // 주입
    }

    public User registerUser(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new CustomException(CustomException.ErrorCode.USER_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        User newUser = new User(registerDto.getUsername(), registerDto.getNickname(), registerDto.getEmail(), encodedPassword);
        return userRepository.save(newUser);
    }

    public Optional<String> loginUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(tokenProvider::generateToken);
    }



    public Optional<User> getUserInfo(String email) {
        return userRepository.findByEmail(email);
    }
}
