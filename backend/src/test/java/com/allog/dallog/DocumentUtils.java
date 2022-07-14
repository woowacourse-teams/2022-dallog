package com.allog.dallog;

import io.restassured.specification.RequestSpecification;

public class DocumentUtils {

    private static RequestSpecification spec;

    private DocumentUtils() {
    }

    public static RequestSpecification getRequestSpecification() {
        return spec;
    }

    public static void setRequestSpecification(final RequestSpecification spec) {
        DocumentUtils.spec = spec;
    }
}
