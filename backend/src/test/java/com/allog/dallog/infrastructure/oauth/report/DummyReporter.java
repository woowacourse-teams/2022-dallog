package com.allog.dallog.infrastructure.oauth.report;

import com.allog.dallog.global.error.Reporter;
import com.allog.dallog.global.error.dto.ErrorReportRequest;

public class DummyReporter implements Reporter {

    @Override
    public void report(final ErrorReportRequest errorReportRequest) {
    }
}
