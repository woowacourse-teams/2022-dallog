package com.allog.dallog.schedule.domain;

import com.allog.dallog.schedule.exception.InvalidScheduleException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SCHEDULES")
public class Schedule {

    private static final int MAX_MEMO_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Embedded
    private Title title;

    @Embedded
    private Period period;

    @Column(nullable = false)
    private String memo;

    protected Schedule() {
    }

    public Schedule(final String title, final LocalDateTime startDateTime,
        final LocalDateTime endDateTime, final String memo) {
        validateMemoLength(memo);
        this.title = new Title(title);
        this.period = new Period(startDateTime, endDateTime);
        this.memo = memo;
    }

    private void validateMemoLength(String memo) {
        if (memo.length() > MAX_MEMO_LENGTH) {
            throw new InvalidScheduleException("일정 메모의 길이는 255를 초과할 수 없습니다.");
        }
    }

    public Long getId() {
        return Id;
    }

    public String getTitle() {
        return title.getValue();
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
}
