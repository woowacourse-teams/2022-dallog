package com.allog.dallog.common.annotation;

import com.allog.dallog.common.config.ExternalApiConfig;
import com.allog.dallog.global.config.JpaConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import({ExternalApiConfig.class, JpaConfig.class})
@ActiveProfiles("test")
@Transactional
public class IntegrationTest {
}
