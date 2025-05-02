package com.gdg.planpal.domain.map.domain.pin;

import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.schedule.domain.StarMapPinSchedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor
@DiscriminatorValue("STAR")
public class StarMapPin extends MapPin {
    @OneToMany(mappedBy = "mapPin", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StarMapPinSchedule> schedules = new ArrayList<>();

    @Override
    public IconType getIconType() {
        return IconType.STAR;
    }
}
