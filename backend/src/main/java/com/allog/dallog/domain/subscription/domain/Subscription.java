package com.allog.dallog.domain.subscription.domain;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.common.BaseEntity;
import com.allog.dallog.domain.member.domain.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "color", nullable = false)
    private Color color;

    @Column(name = "checked", nullable = false)
    private boolean checked;

    protected Subscription() {
    }

    public Subscription(final Member member, final Category category, final Color color) {
        this.member = member;
        this.category = category;
        this.color = color;
        this.checked = true;
    }

    public void change(final Color color, final boolean checked) {
        this.color = color;
        this.checked = checked;
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

    public Color getColor() {
        return color;
    }

    public boolean isChecked() {
        return checked;
    }
}
