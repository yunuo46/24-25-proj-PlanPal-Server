package com.gdg.planpal.infra.oauth.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gdg.planpal.infra.oauth.OauthInfoResponse;
import com.gdg.planpal.infra.oauth.OauthProvider;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleInfoResponse implements OauthInfoResponse {
    private String email;

    private String name;

    private String picture;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public OauthProvider getOauthProvider() {
        return OauthProvider.GOOGLE;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getProfileImageUrl() {
        return picture;
    }
}
