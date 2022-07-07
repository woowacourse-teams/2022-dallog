package com.allog.dallog.schedule.domain;

import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SCHEDULES")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Period period;

    @Embedded
    private Memo memo;

    protected Schedule() {
    }

    public Schedule(final String title, final LocalDateTime startDateTime,
        final LocalDateTime endDateTime, final String memo) {
        this.title = new Title(title);
        this.period = new Period(startDateTime, endDateTime);
        this.memo = new Memo(memo);
    }

    public Long getId() {
        return id;
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
        return memo.getValue();
    }
}
