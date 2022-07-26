package com.allog.dallog.domain.auth.dto;

public class OAuthMember {

    private final String email;
    private final String displayName;
    private final String profileImageUrl;

    public OAuthMember(final String email, final String displayName, final String profileImageUrl) {
        this.email = email;
        this.displayName = displayName;
        this.profileImageUrl = profileImageUrl;
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
}
