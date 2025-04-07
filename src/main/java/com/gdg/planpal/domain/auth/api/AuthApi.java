package com.gdg.planpal.domain.auth.api;

import com.gdg.planpal.domain.auth.application.OauthLoginService;
import com.gdg.planpal.domain.auth.dto.Tokens;
import com.gdg.planpal.domain.auth.dto.response.TokenResponse;
import com.gdg.planpal.domain.auth.util.JwtUtil;
import com.gdg.planpal.infra.oauth.google.GoogleLoginParams;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {

    private final OauthLoginService oauthLoginService;

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestBody GoogleLoginParams params, HttpServletResponse response) {
        Tokens tokens = oauthLoginService.login(params);
        System.out.println("login Kakao finished");
        TokenResponse tokenResponseDto = JwtUtil.setJwtResponse(response, tokens);
        return ResponseEntity.ok(tokenResponseDto);
    }
}
