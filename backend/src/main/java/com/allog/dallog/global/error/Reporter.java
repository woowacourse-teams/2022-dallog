package com.allog.dallog.global.error;

import com.allog.dallog.global.error.dto.ErrorReportRequest;

public interface Reporter {

    void report(final ErrorReportRequest errorReportRequest);
}
