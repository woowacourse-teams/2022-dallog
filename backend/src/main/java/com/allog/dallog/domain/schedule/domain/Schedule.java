package com.allog.dallog.domain.schedule.domain;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.common.BaseEntity;
import com.allog.dallog.domain.schedule.exception.InvalidScheduleException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "schedules")
@Entity
public class Schedule extends BaseEntity {

    private static final int MAX_TITLE_LENGTH = 20;
    private static final int MAX_MEMO_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_id", nullable = false)
    private Category category;

    @Column(name = "title", nullable = false)
    private String title;

    @Embedded
    private Period period;

    @Column(name = "memo", nullable = false)
    private String memo;


    protected Schedule() {
    }

    public Schedule(final Category category, final String title, final LocalDateTime startDateTime,
                    final LocalDateTime endDateTime, final String memo) {
        validateTitleLength(title);
        validateMemoLength(memo);
        this.category = category;
        this.title = title;
        this.period = new Period(startDateTime, endDateTime);
        this.memo = memo;
    }

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartDateTime() {
        return period.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return period.getEndDateTime();
    }

    public String getMemo() {
        return memo;
    }

    public Long getCategoryId() {
        return category.getId();
    }

    public void changeTitle(final String title) {
        validateTitleLength(title);
        this.title = title;
    }

    public void changePeriod(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        this.period = new Period(startDateTime, endDateTime);
    }

    public void changeMemo(final String memo) {
        validateMemoLength(memo);
        this.memo = memo;
    }

    private void validateTitleLength(final String title) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new InvalidScheduleException("일정 제목의 길이는 20을 초과할 수 없습니다.");
        }
    }

    private void validateMemoLength(final String memo) {
        if (memo.length() > MAX_MEMO_LENGTH) {
            throw new InvalidScheduleException("일정 메모의 길이는 255를 초과할 수 없습니다.");
        }
    }
}
