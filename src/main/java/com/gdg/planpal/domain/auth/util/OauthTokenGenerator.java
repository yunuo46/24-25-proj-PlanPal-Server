package com.gdg.planpal.domain.auth.util;

import com.gdg.planpal.domain.auth.dao.RefreshTokenRepository;
import com.gdg.planpal.domain.auth.domain.RefreshToken;
import com.gdg.planpal.domain.auth.dto.Tokens;
import com.gdg.planpal.domain.user.domain.User;
import com.gdg.planpal.domain.user.dto.UserClaim;
import com.gdg.planpal.infra.domain.oauth.OauthInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class OauthTokenGenerator {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public Tokens generate(Long userId, UserClaim userClaim) {

        String subject = userId.toString();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);
        Tokens tokens = tokenProvider.generateTokenDto(authentication, userClaim);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokens.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokens;
    }
}
