package com.allog.dallog.presentation;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.request.MemberUpdateRequest;
import com.allog.dallog.domain.member.dto.response.MemberResponse;
import com.allog.dallog.presentation.auth.AuthenticationPrincipal;

@RequestMapping("/api/members")
@RestController
public class MemberController {

	private final MemberService memberService;

	public MemberController(final MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping("/me")
	public ResponseEntity<MemberResponse> findMe(@AuthenticationPrincipal final LoginMember loginMember) {
		MemberResponse response = memberService.findById(loginMember.getId());
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/me")
	public ResponseEntity<Void> update(@AuthenticationPrincipal LoginMember loginMember,
		@Valid @RequestBody final MemberUpdateRequest request) {
		memberService.update(loginMember.getId(), request);
		return ResponseEntity.noContent().build();
	}
}
