package com.allog.dallog.auth.domain;

import com.allog.dallog.global.entity.BaseEntity;
import com.allog.dallog.member.domain.Member;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "oauth_tokens")
@Entity
public class OAuthToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id", nullable = false)
    private Member member;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    protected OAuthToken() {
    }

    public OAuthToken(final Member member, final String refreshToken) {
        this.member = member;
        this.refreshToken = refreshToken;
    }

    public void change(final String refreshToken) {
        if (!Objects.isNull(refreshToken)) {
            this.refreshToken = refreshToken;
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
