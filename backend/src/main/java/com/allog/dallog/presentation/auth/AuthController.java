package com.allog.dallog.presentation.auth;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.auth.dto.request.TokenCreateRequest;
import com.allog.dallog.domain.auth.dto.response.OAuthLinkResponse;
import com.allog.dallog.domain.auth.dto.response.TokenResponse;
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
    public ResponseEntity<OAuthLinkResponse> generateLink(@PathVariable final String oauthProvider,
                                                          @RequestParam final String redirectUri) {
        OAuthLinkResponse oAuthLinkResponse = new OAuthLinkResponse(authService.generateGoogleLink(redirectUri));
        return ResponseEntity.ok(oAuthLinkResponse);
    }

    @PostMapping("/{oauthProvider}/token")
    public ResponseEntity<TokenResponse> generateToken(@PathVariable final String oauthProvider,
                                                       @Valid @RequestBody final TokenCreateRequest tokenCreateRequest) {
        TokenResponse tokenResponse = authService.generateToken(tokenCreateRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/validate/token")
    public ResponseEntity<Void> validateToken(@AuthenticationPrincipal final LoginMember loginMember) {
        return ResponseEntity.ok().build();
    }
}
