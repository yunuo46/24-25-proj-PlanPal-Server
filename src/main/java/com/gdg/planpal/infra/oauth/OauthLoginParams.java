package com.gdg.planpal.infra.oauth;

public interface OauthLoginParams {
    OauthProvider oauthProvider();
    String getAccessToken();
}
