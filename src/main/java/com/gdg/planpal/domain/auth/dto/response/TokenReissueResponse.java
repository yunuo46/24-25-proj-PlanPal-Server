package com.gdg.planpal.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenReissueResponse {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
}
