package com.gdg.planpal.infra.oauth;

public interface OauthInfoResponse {
    String getEmail();
    String getName();
    OauthProvider getOauthProvider();
    String getProfileImageUrl();
}
