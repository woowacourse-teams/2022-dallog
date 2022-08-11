package com.allog.dallog.domain;

import com.allog.dallog.common.config.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(classes = TestConfig.class)
public class ServiceTest {
}
