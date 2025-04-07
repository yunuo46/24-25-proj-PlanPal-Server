package com.gdg.planpal.infra.oauth;

public interface OauthApiClient {
    OauthProvider oauthProvider();
    OauthInfoResponse requestOauthInfo(String accessToken);
}
