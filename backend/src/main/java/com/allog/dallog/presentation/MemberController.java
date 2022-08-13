package com.allog.dallog.presentation;

import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.composition.application.RegisterService;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.member.dto.MemberUpdateRequest;
import com.allog.dallog.presentation.auth.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final RegisterService registerService;

    public MemberController(final MemberService memberService, final RegisterService registerService) {
        this.memberService = memberService;
        this.registerService = registerService;
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMe(@AuthenticationPrincipal final LoginMember loginMember) {
        MemberResponse response = memberService.findById(loginMember.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> update(@AuthenticationPrincipal LoginMember loginMember,
                                       @RequestBody final MemberUpdateRequest request) {
        memberService.update(loginMember.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal final LoginMember loginMember) {
        registerService.delete(loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
