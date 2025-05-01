package com.gdg.planpal.domain.map.domain.pin;

import com.gdg.planpal.domain.map.domain.Coordinates;
import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.domain.MapBoard;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "icon_type")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class MapPin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapboard_id")
    private MapBoard mapBoard;

    @Embedded
    @Column(nullable = false)
    private Coordinates coordinates;

    @Column(nullable = false)
    private String placeId;

    private String content;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public abstract IconType getIconType();
}

