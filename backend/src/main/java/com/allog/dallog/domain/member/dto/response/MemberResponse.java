package com.allog.dallog.domain.member.dto.response;

import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.SocialType;

public class MemberResponse {

    private Long id;
    private String email;
    private String displayName;
    private String profileImageUrl;
    private SocialType socialType;

    private MemberResponse() {
    }

    public MemberResponse(final Long id, final String email, final String displayName, final String profileImageUrl,
                          final SocialType socialType) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.profileImageUrl = profileImageUrl;
        this.socialType = socialType;
    }

    public MemberResponse(final Member member) {
        this(member.getId(), member.getEmail(), member.getDisplayName(), member.getProfileImageUrl(),
                member.getSocialType());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public SocialType getSocialType() {
        return socialType;
    }
}
