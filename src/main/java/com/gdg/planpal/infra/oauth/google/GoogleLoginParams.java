package com.gdg.planpal.infra.oauth.google;

import com.gdg.planpal.infra.oauth.OauthProvider;
import lombok.*;
import com.gdg.planpal.infra.oauth.OauthLoginParams;

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
