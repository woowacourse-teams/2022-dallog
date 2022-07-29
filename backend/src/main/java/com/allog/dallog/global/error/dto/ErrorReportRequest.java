package com.allog.dallog.global.error.dto;

public class ErrorReportRequest {

    private String requestUri;
    private String requestMethod;
    private String message;

    public ErrorReportRequest(final String requestUri, final String requestMethod, final String message) {
        this.requestUri = requestUri;
        this.requestMethod = requestMethod;
        this.message = message;
    }

    public String getLogMessage() {
        return String.format("[%s] %s - %s", requestMethod, requestUri, message);
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getMessage() {
        return message;
    }

}
