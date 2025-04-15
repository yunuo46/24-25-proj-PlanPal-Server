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
    public ResponseEntity<?> reissue(@CookieValue(name = "refreshToken") String refreshToken) {
        Tokens newTokens = tokenService.reissue(refreshToken);

        TokenResponse tokenResponseDto = JwtUtil.setJwtResponse(newTokens);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestBody GoogleLoginParams params) {
        System.out.println("loginGoogle api call");
        Tokens tokens = oauthLoginService.login(params);
        System.out.println("login google finished");
        TokenResponse tokenResponseDto = JwtUtil.setJwtResponse(tokens);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @GetMapping("/test")
    public void getCurrentUser(Authentication authentication) {
        System.out.println("이름 (getName): " + authentication.getName());
        System.out.println("주체 (getPrincipal): " + authentication.getPrincipal());
        System.out.println("권한 목록:");
        authentication.getAuthorities().forEach(a -> System.out.println(" - " + a.getAuthority()));
        System.out.println("상세 정보 (getDetails): " + authentication.getDetails());
        System.out.println("인증 여부 (isAuthenticated): " + authentication.isAuthenticated());
    }
}
