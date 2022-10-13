package com.allog.dallog.presentation.auth;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.auth.dto.request.TokenRenewalRequest;
import com.allog.dallog.domain.auth.dto.request.TokenRequest;
import com.allog.dallog.domain.auth.dto.response.AccessAndRefreshTokenResponse;
import com.allog.dallog.domain.auth.dto.response.AccessTokenResponse;
import com.allog.dallog.domain.auth.dto.response.OAuthUriResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/{oauthProvider}/oauth-uri")
    public ResponseEntity<OAuthUriResponse> generateLink(@PathVariable final String oauthProvider,
                                                         @RequestParam final String redirectUri) {
        OAuthUriResponse oAuthUriResponse = new OAuthUriResponse(authService.generateGoogleLink(redirectUri));
        return ResponseEntity.ok(oAuthUriResponse);
    }

    @PostMapping("/{oauthProvider}/token")
    public ResponseEntity<AccessAndRefreshTokenResponse> generateAccessAndRefreshToken(
            @PathVariable final String oauthProvider, @Valid @RequestBody final TokenRequest tokenRequest) {
        AccessAndRefreshTokenResponse accessAndRefreshTokenResponse = authService.generateAccessAndRefreshToken(
                tokenRequest);
        return ResponseEntity.ok(accessAndRefreshTokenResponse);
    }

    @PostMapping("/token/access")
    public ResponseEntity<AccessTokenResponse> generateAccessToken(
            @Valid @RequestBody final TokenRenewalRequest tokenRenewalRequest) {
        AccessTokenResponse accessTokenResponse = authService.generateAccessToken(tokenRenewalRequest);
        return ResponseEntity.ok(accessTokenResponse);
    }

    @GetMapping("/validate/token")
    public ResponseEntity<Void> validateToken(@AuthenticationPrincipal final LoginMember loginMember) {
        return ResponseEntity.ok().build();
    }
}
