package com.web.baebaeBE.infra.member.entity;

import com.web.baebaeBE.infra.member.enums.MemberType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String nickname;

  @Column(name="profile_image")
  private String profileImage;

  @Column(name = "member_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private MemberType memberType;

  @Column(name = "refresh_token")
  private String refreshToken;

  @Column(name = "token_expiration_time")
  private LocalDateTime tokenExpirationTime;

  @Column(name = "fcm_token")
  private String fcmToken;


  public Member update(String nickname) {
    this.nickname = nickname;
    return this;
  }

  public void updateRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
  public void updateProfileImage(String profileImageKey){
    this.profileImage = profileImageKey;
  }
  public void updateFcmToken(String fcmToken){
    this.fcmToken = fcmToken;
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