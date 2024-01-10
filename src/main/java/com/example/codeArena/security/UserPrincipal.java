package com.example.codeArena.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    // 기타 필요한 사용자 정보 필드

    // 생성자 및 getter, setter

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
        return String.valueOf(id);
    }

}
