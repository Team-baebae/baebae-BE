package com.web.baebaeBE.domain.fcm.controller;


import com.web.baebaeBE.domain.fcm.dto.FcmRequest;
import com.web.baebaeBE.domain.fcm.entity.FcmToken;
import com.web.baebaeBE.domain.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {

    private final FcmService fcmService;


    @PostMapping("/{memberId}")
    public ResponseEntity<FcmToken> addFcmToken(
            @PathVariable Long memberId,
            @RequestBody FcmRequest.Token request
    ) {
        FcmToken token = fcmService.addFcmToken(memberId, request.getFcmToken());
        return ResponseEntity.ok(token);
    }
}