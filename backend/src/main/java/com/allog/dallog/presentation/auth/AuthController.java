package com.allog.dallog.presentation.auth;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.auth.dto.request.TokenRequest;
import com.allog.dallog.domain.auth.dto.response.OAuthUriResponse;
import com.allog.dallog.domain.auth.dto.response.TokenResponse;
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
    public ResponseEntity<TokenResponse> generateToken(@PathVariable final String oauthProvider,
                                                       @RequestBody final TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.generateToken(tokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/validate/token")
    public ResponseEntity<Void> validateToken(@AuthenticationPrincipal final LoginMember loginMember) {
        return ResponseEntity.ok().build();
    }
}
