package com.allog.dallog.global.presentation;

import com.allog.dallog.domain.auth.exception.EmptyAuthorizationHeaderException;
import com.allog.dallog.domain.auth.exception.InvalidTokenException;
import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.exception.InvalidCategoryException;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.member.exception.InvalidMemberException;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import com.allog.dallog.domain.schedule.exception.InvalidScheduleException;
import com.allog.dallog.domain.subscription.exception.ExistSubscriptionException;
import com.allog.dallog.domain.subscription.exception.InvalidSubscriptionException;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import com.allog.dallog.global.dto.ErrorResponse;
import com.allog.dallog.infrastructure.oauth.exception.OAuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({
            InvalidCategoryException.class,
            InvalidMemberException.class,
            InvalidScheduleException.class,
            InvalidSubscriptionException.class,
            ExistSubscriptionException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidData(final RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({
            EmptyAuthorizationHeaderException.class,
            InvalidTokenException.class,
            NoPermissionException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidAuthorization() {
        ErrorResponse errorResponse = new ErrorResponse("사용자 인증에 실패하였습니다.");
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler({
            NoSuchCategoryException.class,
            NoSuchMemberException.class,
            NoSuchSubscriptionException.class
    })
    public ResponseEntity<ErrorResponse> handleNoSuchData(final RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(OAuthException.class)
    public ResponseEntity<ErrorResponse> handleOAuthException() {
        ErrorResponse errorResponse = new ErrorResponse("OAuth 통신 과정에서 에러가 발생했습니다.");
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestBody() {
        ErrorResponse errorResponse = new ErrorResponse("잘못된 형식의 Request Body 입니다.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException() {
        ErrorResponse errorResponse = new ErrorResponse("예상하지 못한 서버 에러가 발생했습니다.");
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
