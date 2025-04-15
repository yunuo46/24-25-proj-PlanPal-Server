package com.gdg.planpal.domain.auth.application;

import com.gdg.planpal.domain.auth.dao.RefreshTokenRepository;
import com.gdg.planpal.domain.auth.domain.RefreshToken;
import com.gdg.planpal.domain.auth.dto.Tokens;
import com.gdg.planpal.domain.auth.util.OauthTokenGenerator;
import com.gdg.planpal.domain.auth.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final OauthTokenGenerator oauthTokenGenerator;

    public Tokens reissue(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
        }

        RefreshToken oldRefreshToken = refreshTokenRepository.findByValue(refreshToken)
                .orElseThrow(() -> new RuntimeException("DB에 존재하지 않는 Refresh Token입니다."));

        Tokens newTokens = oauthTokenGenerator.generate(oldRefreshToken.getKey());

        RefreshToken newRefreshToken = RefreshToken.builder()
                .key(oldRefreshToken.getKey())
                .value(newTokens.getRefreshToken())
                .build();

        refreshTokenRepository.save(newRefreshToken);

        return newTokens;
    }
}