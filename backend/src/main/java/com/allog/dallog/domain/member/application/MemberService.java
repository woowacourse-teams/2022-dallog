package com.allog.dallog.domain.member.application;

import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.member.dto.MemberUpdateRequest;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private static final String PERSONAL_CATEGORY_NAME = "개인 일정";

    private final MemberRepository memberRepository;
    private final CategoryService categoryService;

    public MemberService(final MemberRepository memberRepository, final CategoryService categoryService) {
        this.memberRepository = memberRepository;
        this.categoryService = categoryService;
    }

    @Transactional
    public MemberResponse save(final Member member) {
        Member newMember = memberRepository.save(member);
        createPersonalCategory(member);

        return new MemberResponse(newMember);
    }

    private void createPersonalCategory(final Member member) {
        Long memberId = member.getId();
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(PERSONAL_CATEGORY_NAME, true);
        categoryService.save(memberId, categoryCreateRequest);
    }

    public MemberResponse findById(final Long id) {
        return new MemberResponse(getMember(id));
    }

    @Transactional
    public void update(final Long id, final MemberUpdateRequest request) {
        Member member = getMember(id);
        member.change(request.getDisplayName());
    }

    public Member getMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(NoSuchMemberException::new);
    }

    public Member getByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(NoSuchMemberException::new);
    }

    public boolean existsByEmail(final String email) {
        return memberRepository.existsByEmail(email);
    }

    public void validateExistsMember(final Long id) {
        if (!memberRepository.existsById(id)) {
            throw new NoSuchMemberException("존재하지 않는 회원입니다.");
        }
    }
}
