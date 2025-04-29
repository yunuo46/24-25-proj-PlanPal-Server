package com.gdg.planpal.domain.map.domain;

import com.gdg.planpal.domain.chatroom.domain.ChatRoom;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "chat_room_id", unique = true)
    private ChatRoom chatRoom;

    @Embedded
    @Column(nullable = false)
    private Coordinates centorCoordinates;

    private Integer zoom;

    private String mapId;

    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MapPin> pins = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
