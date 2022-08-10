package com.allog.dallog.presentation.auth;

import com.allog.dallog.domain.auth.exception.EmptyAuthorizationHeaderException;
import com.allog.dallog.domain.auth.exception.InvalidTokenException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class AuthorizationExtractor {

    private static final String BEARER_TYPE = "Bearer ";

    public static String extract(final HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authorizationHeader)) {
            throw new EmptyAuthorizationHeaderException();
        }

        validateAuthorizationFormat(authorizationHeader);
        return authorizationHeader.substring(BEARER_TYPE.length()).trim();
    }

    private static void validateAuthorizationFormat(final String authorizationHeader) {
        if (!authorizationHeader.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            throw new InvalidTokenException("token 형식이 잘못 되었습니다.");
        }
    }
}
