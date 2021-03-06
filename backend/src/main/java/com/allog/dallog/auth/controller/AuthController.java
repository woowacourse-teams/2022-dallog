package com.allog.dallog.auth.controller;

import com.allog.dallog.auth.dto.OAuthUriResponse;
import com.allog.dallog.auth.dto.TokenRequest;
import com.allog.dallog.auth.dto.TokenResponse;
import com.allog.dallog.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/{oauthProvider}/oauth-uri")
    public ResponseEntity<OAuthUriResponse> generateLink(@PathVariable final String oauthProvider) {
        OAuthUriResponse oAuthUriResponse = new OAuthUriResponse(authService.generateGoogleLink());
        return ResponseEntity.ok(oAuthUriResponse);
    }

    @PostMapping("/{oauthProvider}/token")
    public ResponseEntity<TokenResponse> generateToken(@PathVariable final String oauthProvider,
                                                       @RequestBody final TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.generateTokenWithCode(tokenRequest.getCode());
        return ResponseEntity.ok(tokenResponse);
    }
}
