package com.gdg.planpal.domain.user.domain;

import com.gdg.planpal.infra.domain.oauth.OauthInfoResponse;
import com.gdg.planpal.infra.domain.oauth.OauthProvider;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;

    private String profileImageUrl;

    public static User of(OauthInfoResponse oauthInfoResponse) {
        return User.builder()
                .name(oauthInfoResponse.getName())
                .email(oauthInfoResponse.getEmail())
                .oauthProvider(oauthInfoResponse.getOauthProvider())
                .profileImageUrl(oauthInfoResponse.getProfileImageUrl())
                .build();
    }
}
