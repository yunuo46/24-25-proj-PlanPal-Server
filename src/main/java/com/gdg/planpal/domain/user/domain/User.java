package com.gdg.planpal.domain.user.domain;

import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.infra.domain.oauth.OauthInfoResponse;
import com.gdg.planpal.infra.domain.oauth.OauthProvider;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MapPin> mapPins;

    public static User of(OauthInfoResponse oauthInfoResponse) {
        return User.builder()
                .name(oauthInfoResponse.getName())
                .email(oauthInfoResponse.getEmail())
                .oauthProvider(oauthInfoResponse.getOauthProvider())
                .profileImageUrl(oauthInfoResponse.getProfileImageUrl())
                .build();
    }
}
