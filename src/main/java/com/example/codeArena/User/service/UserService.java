package com.example.codeArena.User.service;

import com.example.codeArena.User.dto.RegisterDto;
import com.example.codeArena.User.model.User;
import com.example.codeArena.User.model.UserRole;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.User.util.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    // 사용자 등록
    public User registerUser(RegisterDto registerDto) throws CustomException {
        logger.debug("사용자 등록 시도: {}", registerDto.getEmail());
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            logger.warn("사용자 등록 실패: 이메일이 이미 존재함 - {}", registerDto.getEmail());
            throw new CustomException(CustomException.ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByNickname(registerDto.getNickname())) {
            logger.warn("사용자 등록 실패: 닉네임이 이미 존재함 - {}", registerDto.getNickname());
            throw new CustomException(CustomException.ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        User newUser = User.builder()
                .username(registerDto.getUsername())
                .nickname(registerDto.getNickname())
                .email(registerDto.getEmail())
                .password(encodedPassword)
                .role(UserRole.USER)
                .build();

        return userRepository.save(newUser);
    }

    // 사용자 로그인
    public Optional<String> loginUser(String email, String password) {
        logger.debug("로그인 시도: {}", email);
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    logger.info("로그인 성공: {}", email);
                    return tokenProvider.generateToken(user);
                })
                .map(Optional::of)
                .orElseThrow(() -> {
                    logger.warn("로그인 실패: {}", email);
                    return new CustomException(CustomException.ErrorCode.INVALID_CREDENTIALS);
                });
    }

    // 사용자 정보 조회
    public Optional<User> getUserInfo(String nickname) {
        logger.debug("사용자 정보 조회: {}", nickname);
        return userRepository.findByNickname(nickname);
    }
}