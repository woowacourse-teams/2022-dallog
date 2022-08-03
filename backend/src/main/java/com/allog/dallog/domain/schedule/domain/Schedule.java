package com.allog.dallog.domain.schedule.domain;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.common.BaseEntity;
import com.allog.dallog.domain.schedule.exception.InvalidScheduleException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private static final int ONE_DAY = 1;
    private static final int MID_NIGHT_HOUR = 0;
    private static final int MID_NIGHT_MINUTE = 0;

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

    public void change(final String title, final LocalDateTime startDateTime, final LocalDateTime endDateTime,
                       final String memo) {
        validateTitleLength(title);
        validateMemoLength(memo);
        this.title = title;
        this.period = new Period(startDateTime, endDateTime);
        this.memo = memo;
    }

    public long calculateHourDifference() {
        return ChronoUnit.HOURS.between(getStartDateTime(), getEndDateTime());
    }

    public boolean isDayDifferent() {
        long dayDifference = ChronoUnit.DAYS.between(LocalDate.from(getStartDateTime()),
                LocalDate.from(getEndDateTime()));
        return dayDifference >= ONE_DAY;
    }

    public boolean isMidNight() {
        return getStartDateTime().getHour() == MID_NIGHT_HOUR && getStartDateTime().getMinute() == MID_NIGHT_MINUTE;
    }

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
        return category.getId();
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

    public Category getCategory() {
        return category;
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
