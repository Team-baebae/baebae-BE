package com.web.baebaeBE.domain.Follow.controller;


import com.web.baebaeBE.domain.Follow.controller.api.FollowApi;
import com.web.baebaeBE.domain.Follow.dto.FollowResponse;
import com.web.baebaeBE.domain.Follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController implements FollowApi {

    private final FollowService followService;


    @PostMapping("/{followerId}/{followingId}")
    public ResponseEntity<Void> FollowMember(
            @PathVariable Long followerId,
            @PathVariable Long followingId
    ) {
        followService.followMember(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{followerId}/{followingId}")
    public ResponseEntity<Void> DeleteFollower(
            @PathVariable Long followerId,
            @PathVariable Long followingId
    ) {
        followService.deleteFollower(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/followers/{memberId}")
    public ResponseEntity<Page<FollowResponse.FollowMemberResponse>> getFollowerList(
            @PathVariable Long memberId,
            Pageable page
    ) {
        return ResponseEntity.ok(followService.getFollowerList(memberId, page));
    }

    @GetMapping("followings/{memberId}")
    public ResponseEntity<Page<FollowResponse.FollowMemberResponse>> getFollowingList(
            @PathVariable Long memberId,
            Pageable page
    ) {
        return ResponseEntity.ok(followService.getFollowingList(memberId, page));
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
