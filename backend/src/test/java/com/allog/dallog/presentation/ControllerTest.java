package com.allog.dallog.presentation;

import com.allog.dallog.common.config.OAuthConfig;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;

@AutoConfigureRestDocs
@Import(OAuthConfig.class)
public class ControllerTest {
}
