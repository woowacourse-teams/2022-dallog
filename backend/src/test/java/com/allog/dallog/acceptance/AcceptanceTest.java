package com.allog.dallog.acceptance;

import com.allog.dallog.common.DatabaseCleaner;
import com.allog.dallog.common.config.ExternalApiConfig;
import com.allog.dallog.domain.auth.domain.TokenRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ExternalApiConfig.class)
@ActiveProfiles("test")
abstract class AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleaner.execute();
        tokenRepository.deleteAll();
    }
}

