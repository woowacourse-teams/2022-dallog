package com.allog.dallog.domain.auth.domain;

import com.allog.dallog.domain.auth.exception.NoSuchTokenException;

public class AuthToken {

	private String accessToken;
	private String refreshToken;

	public AuthToken(final String accessToken, final String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void validateHasSameRefreshToken(final String otherRefreshToken) {
		if (!refreshToken.equals(otherRefreshToken)) {
			throw new NoSuchTokenException("회원의 리프레시 토큰이 아닙니다.");
		}
	}
}
