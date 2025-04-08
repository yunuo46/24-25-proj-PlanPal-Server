package com.gdg.planpal.infra.domain.oauth.google;

import com.gdg.planpal.infra.domain.oauth.OauthProvider;
import lombok.*;
import com.gdg.planpal.infra.domain.oauth.OauthLoginParams;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginParams implements OauthLoginParams {
    private String authorizationCode;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.GOOGLE;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        return body;
    }
}
