package com.allog.dallog.member.domain;

import com.allog.dallog.global.domain.BaseEntity;
import com.allog.dallog.member.exception.InvalidMemberException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "members")
@Entity
public class Member extends BaseEntity {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$");
    private static final int MAX_DISPLAY_NAME_LENGTH = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "profile_image_url", nullable = false)
    private String profileImageUrl;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    protected Member() {
    }

    public Member(final String email, final String profileImageUrl, final String displayName,
                  final SocialType socialType) {
        validateEmail(email);
        validateDisplayName(displayName);

        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.displayName = displayName;
        this.socialType = socialType;
    }

    private void validateEmail(final String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidMemberException("이메일 형식이 올바르지 않습니다.");
        }
    }

    private void validateDisplayName(final String displayName) {
        if (displayName.isEmpty() || displayName.length() > MAX_DISPLAY_NAME_LENGTH) {
            throw new InvalidMemberException("이름 형식이 올바르지 않습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SocialType getSocialType() {
        return socialType;
    }
}
