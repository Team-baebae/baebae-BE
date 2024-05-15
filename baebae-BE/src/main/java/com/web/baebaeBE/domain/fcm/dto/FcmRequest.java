package com.web.baebaeBE.domain.fcm.dto;

import com.web.baebaeBE.domain.member.entity.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class FcmRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateToken{
        private String fcmToken;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateToken{
        private String oldFcmToken;
        private String newFcmToken;
    }

}
