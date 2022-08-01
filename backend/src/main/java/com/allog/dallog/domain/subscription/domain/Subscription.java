package com.allog.dallog.domain.subscription.domain;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.subscription.exception.InvalidSubscriptionException;
import com.allog.dallog.global.domain.BaseEntity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "subscriptions")
@Entity
public class Subscription extends BaseEntity {

    private static final Pattern COLOR_PATTERN = Pattern.compile("^#[a-fA-F\\d]{6}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_id", nullable = false)
    private Category category;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "checked", nullable = false)
    private boolean checked;

    protected Subscription() {
    }

    public Subscription(final Member member, final Category category, final String color) {
        validateColor(color);

        this.member = member;
        this.category = category;
        this.color = color;
        this.checked = true;
    }

    private void validateColor(final String color) {
        Matcher matcher = COLOR_PATTERN.matcher(color);
        if (!matcher.matches()) {
            throw new InvalidSubscriptionException("(" + color + ")는 올바른 색 정보 형식이 아닙니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Category getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }

    public boolean isChecked() {
        return checked;
    }
}
