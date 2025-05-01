package com.gdg.planpal.domain.map.domain.pin;

import com.gdg.planpal.domain.map.domain.IconType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("STAR")
public class StarMapPin extends MapPin {
    @OneToMany(mappedBy = "mapPin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StarMapPinSchedule> schedules = new ArrayList<>();

    @Override
    public IconType getIconType() {
        return IconType.STAR;
    }
}
