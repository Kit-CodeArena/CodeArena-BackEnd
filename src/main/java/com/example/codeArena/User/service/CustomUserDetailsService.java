package com.example.codeArena.User.service;

import com.example.codeArena.User.model.User;
import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.USER_NOT_FOUND));
        logger.info("사용자 이름으로 로드된 사용자: {}", appUser.getUsername());

        // UserPrincipal을 반환하도록 변경
        return new UserPrincipal(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getPassword(),
                appUser.getNickname(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + appUser.getRole()))
        );
    }
}
