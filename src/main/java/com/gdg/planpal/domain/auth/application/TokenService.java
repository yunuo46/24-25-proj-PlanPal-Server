package com.gdg.planpal.domain.auth.application;

import com.gdg.planpal.domain.auth.dao.RefreshTokenRepository;
import com.gdg.planpal.domain.auth.domain.RefreshToken;
import com.gdg.planpal.domain.auth.dto.Tokens;
import com.gdg.planpal.domain.auth.util.OauthTokenGenerator;
import com.gdg.planpal.domain.auth.util.TokenProvider;
import com.gdg.planpal.domain.user.dao.UserRepository;
import com.gdg.planpal.domain.user.domain.User;
import com.gdg.planpal.domain.user.dto.UserClaim;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final OauthTokenGenerator oauthTokenGenerator;
    private final UserRepository userRepository;

    @Transactional
    public Tokens reissue(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
        }

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByValue(refreshToken)
                .orElseThrow(() -> new RuntimeException("DB에 존재하지 않는 Refresh Token입니다."));

        Long memberId = Long.valueOf(refreshTokenEntity.getKey());
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        UserClaim userClaim = UserClaim.of(user);
        Tokens newTokens = oauthTokenGenerator.generate(user.getId(), userClaim);

        refreshTokenEntity.updateValue(newTokens.getRefreshToken());

        return newTokens;
    }

    public void deleteRefreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        refreshTokenRepository.deleteByValue(refreshToken);
    }
}