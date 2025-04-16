package com.gdg.planpal.domain.auth.api;

import com.gdg.planpal.domain.auth.application.OauthLoginService;
import com.gdg.planpal.domain.auth.application.TokenService;
import com.gdg.planpal.domain.auth.dto.Tokens;
import com.gdg.planpal.domain.auth.dto.response.TokenResponse;
import com.gdg.planpal.domain.auth.util.JwtUtil;
import com.gdg.planpal.infra.domain.oauth.google.GoogleLoginParams;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {

    private final OauthLoginService oauthLoginService;
    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue(name = "refreshToken") String refreshToken, HttpServletResponse response) {
        Tokens newTokens = tokenService.reissue(refreshToken);

        TokenResponse tokenResponseDto = JwtUtil.setJwtResponse(response, newTokens);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestBody GoogleLoginParams params,  HttpServletResponse response) {
        System.out.println("loginGoogle api call");
        Tokens tokens = oauthLoginService.login(params);
        System.out.println("login google finished");
        TokenResponse tokenResponseDto = JwtUtil.setJwtResponse(response, tokens);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refreshToken") String refreshToken) {
        tokenService.deleteRefreshToken(refreshToken);
        return ResponseEntity.ok().build();
    }
}
