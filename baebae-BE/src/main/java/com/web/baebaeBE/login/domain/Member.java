package com.web.baebaeBE.login.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String nickname;

    @Column(name = "member_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Column(name = "refresh_token")
    private String refreshToken;

    private LocalDateTime tokenExpirationTime;


    public Member update(String nickname){
        this.nickname = nickname;
        return this;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateTokenExpirationTime(LocalDateTime time) {
        this.tokenExpirationTime = time;
    }

    @Override // 권한 반환 메서드
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //사용자가 가지고 있는 권한 리스트 반환 -> "user" 권한 반환
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}