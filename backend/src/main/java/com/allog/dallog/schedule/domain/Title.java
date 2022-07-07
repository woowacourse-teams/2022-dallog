package com.allog.dallog.schedule.domain;

import com.allog.dallog.schedule.exception.InvalidScheduleException;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

    private static final int MAX_TITLE_LENGTH = 20;

    @Column(name = "title", length = 20, nullable = false)
    private String value;

    protected Title() {
    }

    public Title(final String value) {
        validateLength(value);
        this.value = value;
    }

    private void validateLength(final String value) {
        if (value.length() > MAX_TITLE_LENGTH) {
            throw new InvalidScheduleException("일정 제목의 길이는 20을 초과할 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
