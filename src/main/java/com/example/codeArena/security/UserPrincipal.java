package com.example.codeArena.security;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Getter
public class UserPrincipal implements UserDetails {
    private static final Logger logger = LoggerFactory.getLogger(UserPrincipal.class);

    private final String id;
    private final String username;
    private final String password;
    private final String nickname;
    private final Collection<? extends GrantedAuthority> authorities;

    // 생성자 추가
    public UserPrincipal(String id, String username, String password, String nickname, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부 반환 (여기서는 항상 true로 설정)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부 반환 (여기서는 항상 true로 설정)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부 반환 (여기서는 항상 true로 설정)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부 반환 (여기서는 항상 true로 설정)
    }

    // 사용자 ID 반환 메소드
    public String getId() {
        logger.debug("User ID retrieved: {}", id);
        return String.valueOf(id);
    }
}
