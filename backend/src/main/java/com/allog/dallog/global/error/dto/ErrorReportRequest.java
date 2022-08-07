package com.allog.dallog.global.error.dto;

import javax.servlet.http.HttpServletRequest;

public class ErrorReportRequest {

    private static final String ERROR_REPORT_FORMAT = "[%s] %s - %s";

    private final HttpServletRequest request;
    private final Exception exception;

    public ErrorReportRequest(final HttpServletRequest request, final Exception exception) {
        this.request = request;
        this.exception = exception;
    }

    public String getLogMessage() {
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        String exceptionMessage = exception.getMessage();

        return String.format(ERROR_REPORT_FORMAT, requestMethod, requestUri, exceptionMessage);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public Exception getException() {
        return exception;
    }
}
