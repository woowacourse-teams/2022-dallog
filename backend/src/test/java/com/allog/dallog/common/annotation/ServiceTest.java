package com.allog.dallog.common.annotation;

import com.allog.dallog.common.config.ExternalApiConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(ExternalApiConfig.class)
@ActiveProfiles("test")
@Transactional
public class ServiceTest {
}
