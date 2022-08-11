package com.allog.dallog.presentation;

import com.allog.dallog.common.config.TestConfig;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;

@AutoConfigureRestDocs
@Import(TestConfig.class)
public class ControllerTest {
}
