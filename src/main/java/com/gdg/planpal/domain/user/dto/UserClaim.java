package com.gdg.planpal.domain.user.dto;

import com.gdg.planpal.domain.user.domain.User;
import com.gdg.planpal.infra.domain.oauth.OauthInfoResponse;
import com.gdg.planpal.infra.domain.oauth.OauthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserClaim {
    private String name;
    private String email;
    private OauthProvider oauthProvider;
    private String profileImageUrl;

    public static UserClaim of(User user) {
        return UserClaim.builder()
                .name(user.getName())
                .email(user.getEmail())
                .oauthProvider(user.getOauthProvider())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}