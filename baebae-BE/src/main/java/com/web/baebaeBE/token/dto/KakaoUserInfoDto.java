package com.web.baebaeBE.token.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfoDto {
    private String id;
    //회원 번호
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;
    //카카오 계정 정보
    @Getter
    @Setter
    public static class KakaoAccount {
        private String email;
        // 카카오 계정 대표 이메일
        private Profile profile;
        // 프로필 정보
        @Getter @Setter
        public static class Profile {

            private String nickname;
            //닉네임
            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;
            //썸네일 이미지 Url

        }

    }
}
