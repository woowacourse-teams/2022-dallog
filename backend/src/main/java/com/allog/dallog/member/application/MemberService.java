package com.allog.dallog.member.application;

import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.member.dto.request.MemberUpdateRequest;
import com.allog.dallog.member.dto.response.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse findById(final Long id) {
        return new MemberResponse(memberRepository.getById(id));
    }

    @Transactional
    public void update(final Long id, final MemberUpdateRequest request) {
        Member member = memberRepository.getById(id);
        member.change(request.getDisplayName());
    }
}
