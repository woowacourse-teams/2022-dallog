package com.allog.dallog.presentation;

import com.allog.dallog.common.config.ExternalApiConfig;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;

@AutoConfigureRestDocs
@Import(ExternalApiConfig.class)
public class ControllerTest {
}
