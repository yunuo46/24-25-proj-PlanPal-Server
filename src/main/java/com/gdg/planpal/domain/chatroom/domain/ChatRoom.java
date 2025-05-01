package com.gdg.planpal.domain.chatroom.domain;

import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, updatable = false)
    private String inviteCode;

    @Column(nullable = false)
    private Integer limitUsers;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private String destination;

    private String thumbnailUrl;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserChatRoom> userChatRooms = new ArrayList<>();

    @OneToOne(mappedBy = "chatRoom")
    private MapBoard mapBoard;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void updateInfo(String name, String destination, String thumbnailUrl) {
        Optional.ofNullable(name)
                .filter(n -> !n.isBlank())
                .ifPresent(n -> this.name = n);
        this.destination = destination;
        this.thumbnailUrl = thumbnailUrl;
    }
}
