package com.gdg.planpal.domain.auth.util;

import com.gdg.planpal.domain.auth.dto.Tokens;
import com.gdg.planpal.domain.auth.dto.response.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JwtUtil {

    public static void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(5))
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }

    public static TokenResponse setJwtResponse(HttpServletResponse response, Tokens tokenDto) {
        TokenResponse tokenResponseDto = TokenResponse.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .accessTokenExpiresIn(tokenDto.getAccessTokenExpiresIn())
                .build();

        setRefreshTokenInCookie(response, tokenDto.getRefreshToken());
        return tokenResponseDto;
    }

}
