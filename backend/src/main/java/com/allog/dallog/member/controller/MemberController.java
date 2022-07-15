package com.allog.dallog.member.controller;

import com.allog.dallog.auth.dto.LoginMember;
import com.allog.dallog.auth.support.AuthenticationPrincipal;
import com.allog.dallog.global.dto.CommonResponse;
import com.allog.dallog.member.dto.MemberResponse;
import com.allog.dallog.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<MemberResponse>> findMe(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse response = memberService.findById(loginMember.getId());
        return ResponseEntity.ok(new CommonResponse<>(response));
    }
}
