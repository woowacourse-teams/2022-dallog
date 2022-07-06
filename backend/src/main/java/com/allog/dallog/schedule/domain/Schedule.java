package com.allog.dallog.schedule.domain;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String title;

    @Embedded
    private Period period;

    @Column(nullable = false)
    private String memo;

    protected Schedule() {
    }

    public Schedule(final String title, final LocalDateTime startDateTime,
        final LocalDateTime endDateTime, final String memo) {
        this.title = title;
        this.period = new Period(startDateTime, endDateTime);
        this.memo = memo;
    }

    public Long getId() {
        return Id;
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
}
