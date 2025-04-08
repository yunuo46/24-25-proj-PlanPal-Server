package com.gdg.planpal.infra.domain.oauth.google;

import com.gdg.planpal.infra.domain.oauth.OauthProvider;
import lombok.*;
import com.gdg.planpal.infra.domain.oauth.OauthLoginParams;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginParams implements OauthLoginParams {
    private String accessToken;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.GOOGLE;
    }
}
