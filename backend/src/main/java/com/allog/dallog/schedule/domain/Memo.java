package com.allog.dallog.schedule.domain;

import com.allog.dallog.schedule.exception.InvalidScheduleException;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Memo {

    private static final int MAX_MEMO_LENGTH = 255;

    @Column(name = "memo", nullable = false)
    private String value;

    protected Memo() {
    }

    public Memo(final String value) {
        validateLength(value);
        this.value = value;
    }

    private void validateLength(final String memo) {
        if (memo.length() > MAX_MEMO_LENGTH) {
            throw new InvalidScheduleException("일정 메모의 길이는 255를 초과할 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
