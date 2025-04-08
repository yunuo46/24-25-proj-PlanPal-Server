package com.gdg.planpal.infra.domain.oauth;

public interface OauthLoginParams {
    OauthProvider oauthProvider();
    String getAccessToken();
}
