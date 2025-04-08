package com.gdg.planpal.infra.domain.oauth;

public interface OauthApiClient {
    OauthProvider oauthProvider();
    OauthInfoResponse requestOauthInfo(String accessToken);
}
