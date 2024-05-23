package com.web.baebaeBE.domain.fcm.controller;


import com.web.baebaeBE.domain.fcm.controller.api.FcmApi;
import com.web.baebaeBE.domain.fcm.dto.FcmRequest;
import com.web.baebaeBE.domain.fcm.entity.FcmToken;
import com.web.baebaeBE.domain.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController implements FcmApi {

    private final FcmService fcmService;


    @PostMapping("/{memberId}")
    public ResponseEntity<Void> addFcmToken(
            @PathVariable Long memberId,
            @RequestBody FcmRequest.CreateToken request
    ) {
        fcmService.addFcmToken(memberId, request.getFcmToken());
        return ResponseEntity.ok().build();
    }

    /*@PutMapping("/{memberId}")
    public ResponseEntity<Void> updateFcmToken(
            @PathVariable Long memberId,
            @RequestBody FcmRequest.UpdateToken request
    ) {
        fcmService.updateFcmToken(request.getOldFcmToken(), request.getNewFcmToken(), memberId);
        return ResponseEntity.ok().build();
    }*/
}
