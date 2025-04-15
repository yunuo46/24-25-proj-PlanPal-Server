package com.gdg.planpal.domain.auth.application;

import com.gdg.planpal.domain.user.dto.UserClaim;
import com.gdg.planpal.infra.domain.oauth.OauthInfoResponse;
import com.gdg.planpal.infra.domain.oauth.OauthLoginParams;
import com.gdg.planpal.infra.domain.oauth.RequestOauthInfoService;
import com.gdg.planpal.domain.auth.dto.Tokens;
import com.gdg.planpal.domain.auth.util.OauthTokenGenerator;
import com.gdg.planpal.domain.user.dao.UserRepository;
import com.gdg.planpal.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthLoginService {
    private final UserRepository userRepository;
    private final OauthTokenGenerator oauthTokensGenerator;
    private final RequestOauthInfoService requestOauthInfoService;

    public Tokens login(OauthLoginParams params) {
        OauthInfoResponse oauthInfoResponse = requestOauthInfoService.request(params);
        User user = findOrCreateMember(oauthInfoResponse);
        UserClaim userClaim = UserClaim.of(user);
        return oauthTokensGenerator.generate(user.getId(), userClaim);
    }

    private User findOrCreateMember(OauthInfoResponse oauthInfoResponse) {
        return userRepository.findByEmail(oauthInfoResponse.getEmail())
                .orElseGet(() -> newMember(oauthInfoResponse));
    }

    private User newMember(OauthInfoResponse oauthInfoResponse) {
        User user = User.of(oauthInfoResponse);
        return userRepository.save(user);
    }
}
