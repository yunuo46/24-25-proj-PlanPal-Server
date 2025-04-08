package com.gdg.planpal.infra.domain.oauth;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RequestOauthInfoService {
    private final Map<OauthProvider, OauthApiClient> clients;

    public RequestOauthInfoService(List<OauthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OauthApiClient::oauthProvider, Function.identity())
        );
    }

    public OauthInfoResponse request(OauthLoginParams params) {
        OauthApiClient client = clients.get(params.oauthProvider());
        return client.requestOauthInfo(params.getAccessToken());
    }
}
