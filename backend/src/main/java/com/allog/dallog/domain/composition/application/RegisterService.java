package com.allog.dallog.domain.composition.application;

import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.member.application.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class RegisterService {

    private final MemberService memberService;
    private final CategoryService categoryService;

    public RegisterService(final MemberService memberService, final CategoryService categoryService) {
        this.memberService = memberService;
        this.categoryService = categoryService;
    }

    @Transactional
    public void deleteByMemberId(final Long memberId) {
        categoryService.deleteByMemberId(memberId);
        memberService.deleteById(memberId);
    }
}
