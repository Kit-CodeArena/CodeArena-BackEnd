package com.example.codeArena.User.service;

import com.example.codeArena.User.dto.RegisterDto;
import com.example.codeArena.User.exception.CustomException;
import com.example.codeArena.User.model.User;
import com.example.codeArena.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new CustomException(CustomException.ErrorCode.USER_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        User newUser = new User(registerDto.getUsername(), registerDto.getEmail(), encodedPassword);
        return userRepository.save(newUser);
    }

    public Optional<User> loginUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public Optional<User> getUserInfo(String email) {
        return userRepository.findByEmail(email);
    }
}
