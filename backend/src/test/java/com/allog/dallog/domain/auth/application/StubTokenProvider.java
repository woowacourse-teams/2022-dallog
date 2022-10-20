package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

public class StubTokenProvider implements TokenProvider {

    private final SecretKey key;
    private final long accessTokenValidityInMilliseconds = 0;
    private final long refreshTokenValidityInMilliseconds = 360000;

    public StubTokenProvider(final String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String createAccessToken(final String payload) {
        return createToken(payload, accessTokenValidityInMilliseconds);
    }

    @Override
    public String createRefreshToken(final String payload) {
        return createToken(payload, refreshTokenValidityInMilliseconds);
    }

    private String createToken(final String payload, final Long validityInMilliseconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String getPayload(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public void validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("권한이 없습니다.");
        }
    }
}
