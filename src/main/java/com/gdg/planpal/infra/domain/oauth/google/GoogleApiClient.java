package com.gdg.planpal.infra.domain.oauth.google;

import com.gdg.planpal.infra.domain.oauth.OauthApiClient;
import com.gdg.planpal.infra.domain.oauth.OauthInfoResponse;
import com.gdg.planpal.infra.domain.oauth.OauthProvider;
import org.springframework.beans.factory.annotation.Value;
import lombok.*;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GoogleApiClient implements OauthApiClient {
    @Value("${oauth.google.url.api}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.GOOGLE;
    }

    @Override
    public OauthInfoResponse requestOauthInfo(String accessToken) {
        String url = apiUrl + "/user/me";
        System.out.println(accessToken);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        return restTemplate.postForObject(url, request, GoogleInfoResponse.class);
    }
}
